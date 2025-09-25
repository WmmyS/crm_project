#!/bin/bash

# Script para modo desenvolvimento local (sem Docker para a aplicação)
# Uso: ./start-dev.sh

echo "🚀 Iniciando CRM em modo DESENVOLVIMENTO LOCAL..."
echo ""

# Função de cleanup ao sair
    echo "✅ CRM parado com sucesso!"
    exit 0
}

# Configurar trap para cleanup
trap cleanup SIGINT SIGTERM

# Verificar se o PostgreSQL está rodando
check_postgres() {
    echo "🔍 Verificando PostgreSQL..."
    
    if ! docker-compose ps postgres | grep -q "Up"; then
        echo "🐘 Iniciando PostgreSQL via Docker..."
        docker-compose up -d postgres
        
        # Aguardar PostgreSQL estar pronto
        echo "⏳ Aguardando PostgreSQL inicializar..."
        sleep 5
        
        # Verificar se está rodando
        for i in {1..30}; do
            if docker-compose ps postgres | grep -q "Up"; then
                echo "✅ PostgreSQL está rodando!"
                break
            fi
            echo "⏳ Aguardando PostgreSQL... ($i/30)"
            sleep 1
        done
    else
        echo "✅ PostgreSQL já está rodando!"
    fi
}

# Função principal
main() {
    # Verificar PostgreSQL
    check_postgres
    
    echo ""
    echo "� Iniciando em modo DEV com hot reload..."
    echo "📝 Pressione Ctrl+C para parar a aplicação"
    echo "💡 O DevTools fará reload automático quando você alterar arquivos!"
    echo "🌐 Aplicação estará disponível em: http://localhost:8080"
    echo ""
    
    # Definir profile de desenvolvimento
    export SPRING_PROFILES_ACTIVE=dev
    
    # Configurações para desenvolvimento local
    export DB_HOST=localhost
    export DB_PORT=5432
    export DB_NAME=crm_db
    export DB_USER=crm_user
    export DB_PASSWORD=crm_password
    
    # Executar aplicação Spring Boot com Maven
    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true -Dspring.devtools.livereload.enabled=true"
}

# Executar função principal
main