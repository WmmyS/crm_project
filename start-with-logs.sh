#!/bin/bash

# Script para gerenciar CRM com logs visíveis
# Uso: ./start-with-logs.sh

echo "🚀 Iniciando CRM com logs visíveis..."
echo "📝 Pressione Ctrl+C para parar a aplicação"
echo ""

# Iniciar em foreground para ver logs
docker-compose up

# Quando Ctrl+C for pressionado, limpar containers
echo ""
echo "🧹 Limpando containers..."
docker-compose down
echo "✅ CRM parado com sucesso!"