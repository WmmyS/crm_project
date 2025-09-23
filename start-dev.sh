#!/bin/bash

echo "🚀 Script de desenvolvimento local do CRM Application"

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não está instalado."
    echo "💡 Use './start.sh' para execução completa com Docker (recomendado)"
    echo "💡 Ou instale o Maven: sudo apt install maven"
    exit 1
fi

# Verificar se o Docker está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado. Por favor, instale o Docker Compose primeiro."
    exit 1
fi

echo "✅ Dependências verificadas com sucesso!"

# Iniciar apenas o PostgreSQL
echo "🐳 Iniciando PostgreSQL..."
docker-compose up postgres -d

echo "⏳ Aguardando PostgreSQL inicializar..."
sleep 5

# Limpar builds anteriores
echo "🧹 Limpando builds anteriores..."
mvn clean > /dev/null 2>&1

# Compilar a aplicação
echo "🔨 Compilando a aplicação..."
mvn package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Erro ao compilar a aplicação. Verifique o código e tente novamente."
    exit 1
fi

echo "✅ Aplicação compilada com sucesso!"

# Executar a aplicação
echo "🚀 Iniciando aplicação em modo desenvolvimento..."
echo "   📍 Profile: dev (desenvolvimento local)"
echo "   🗄️  PostgreSQL: Container Docker"
echo "   ☕ Aplicação: Processo local"

mvn spring-boot:run -Dspring-boot.run.profiles=dev