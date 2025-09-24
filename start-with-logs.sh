#!/bin/bash

# Script para gerenciar CRM com logs visíveis e rebuild automático
# Uso: ./start-with-logs.sh [--rebuild]

REBUILD=false

# Verificar se --rebuild foi passado como parâmetro
if [[ "$1" == "--rebuild" ]]; then
    REBUILD=true
fi

# Função para detectar se houve mudanças no código
check_for_changes() {
    echo "� Verificando mudanças no código..."
    
    # Verificar se existe um container rodando
    if docker-compose ps | grep -q "Up"; then
        echo "📦 Container já está rodando"
        
        # Verificar timestamp do último build vs mudanças recentes no código
        LAST_BUILD=$(docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.CreatedAt}}" | grep "www_crm-app" | head -1 | awk '{print $3 " " $4}')
        
        # Verificar se há arquivos .java ou pom.xml modificados recentemente
        RECENT_CHANGES=$(find ./src -name "*.java" -newer docker-compose.yml 2>/dev/null | wc -l)
        POM_CHANGES=$(find . -name "pom.xml" -newer docker-compose.yml 2>/dev/null | wc -l)
        
        if [[ $RECENT_CHANGES -gt 0 ]] || [[ $POM_CHANGES -gt 0 ]]; then
            echo "⚠️  Detectadas mudanças no código desde o último build!"
            echo "� Será necessário rebuild do container..."
            return 0  # Precisa rebuild
        else
            echo "✅ Código não foi modificado desde o último build"
            return 1  # Não precisa rebuild
        fi
    else
        echo "📦 Nenhum container rodando"
        return 0  # Precisa build inicial
    fi
}

# Função para fazer rebuild
do_rebuild() {
    echo ""
    echo "🔨 Fazendo rebuild do container..."
    echo "⏳ Isso pode levar alguns minutos..."
    
    # Parar containers existentes
    docker-compose down
    
    # Rebuild sem cache para garantir que as mudanças sejam aplicadas
    docker-compose build --no-cache
    
    if [[ $? -eq 0 ]]; then
        echo "✅ Rebuild concluído com sucesso!"
    else
        echo "❌ Erro durante o rebuild!"
        exit 1
    fi
}

# Função principal
main() {
    echo "🚀 Iniciando CRM com logs visíveis..."
    echo ""
    
    # Se --rebuild foi especificado, sempre fazer rebuild
    if [[ $REBUILD == true ]]; then
        echo "🔄 Rebuild forçado via parâmetro --rebuild"
        do_rebuild
    else
        # Verificar automaticamente se precisa rebuild
        if check_for_changes; then
            echo ""
            read -p "❓ Detectadas mudanças. Fazer rebuild automático? (y/N): " -n 1 -r
            echo ""
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                do_rebuild
            else
                echo "⚠️  Continuando sem rebuild - mudanças podem não aparecer!"
            fi
        fi
    fi
    
    echo ""
    echo "🚀 Iniciando aplicação..."
    echo "📝 Pressione Ctrl+C para parar a aplicação"
    echo "💡 Dica: Use './start-with-logs.sh --rebuild' para forçar rebuild"
    echo ""
    
    # Iniciar em foreground para ver logs
    docker-compose up
}

# Função de cleanup ao sair
cleanup() {
    echo ""
    echo "🧹 Limpando containers..."
    docker-compose down
    echo "✅ CRM parado com sucesso!"
    exit 0
}

# Configurar trap para cleanup
trap cleanup SIGINT SIGTERM

# Executar função principal
main