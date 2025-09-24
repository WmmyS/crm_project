#!/bin/bash

echo "� Modo DESENVOLVIMENTO - Sempre rebuild + logs"
echo "🚀 Iniciando CRM com rebuild automático..."
echo ""

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

# Verificar se o Docker está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado. Por favor, instale o Docker Compose primeiro."
    exit 1
fi

echo "✅ Docker Compose encontrado!"

# Sempre parar containers existentes
echo "� Parando containers existentes..."
docker-compose down

# Sempre fazer rebuild
echo "🔨 Fazendo rebuild do container..."
echo "⏳ Aguarde... isso pode levar alguns minutos"
docker-compose build --no-cache

if [[ $? -eq 0 ]]; then
    echo "✅ Rebuild concluído com sucesso!"
else
    echo "❌ Erro durante o rebuild!"
    exit 1
fi

echo ""
echo "🚀 Iniciando aplicação com logs..."
echo "📝 Pressione Ctrl+C para parar a aplicação"
echo ""

# Iniciar em foreground para ver logs
docker-compose up