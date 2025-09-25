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
    echo "� Verificando status dos containers..."
    
    # Verificar se existe um container rodando
    if docker-compose ps | grep -q "Up"; then
        echo "📦 Container já está rodando"
        return 1  # Não precisa rebuild
    else
        echo "📦 Nenhum container rodando"
        # Verificar se existe imagem local
        if docker images | grep -q "www_crm-app"; then
            echo "🖥️  Imagem local encontrada"
            return 1  # Não precisa rebuild
        else
            echo "🔨 Imagem não encontrada, será necessário build inicial"
            return 0  # Precisa build inicial
        fi
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
            echo "🔨 Fazendo build inicial..."
            do_rebuild
        else
            echo "✅ Usando imagem existente"
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