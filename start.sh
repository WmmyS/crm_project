#!/bin/bash

echo "🚀 Script de inicialização do CRM Application"

# Verificar se o Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado. Por favor, instale o Docker primeiro."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado. Por favor, instale o Docker Compose primeiro."
    exit 1
fi

echo "✅ Dependências verificadas com sucesso!"

# Parar containers anteriores (se existirem)
echo "🛑 Parando containers anteriores..."
docker-compose down > /dev/null 2>&1

# Remover imagem anterior da aplicação para forçar rebuild
echo "🧹 Limpando imagem anterior da aplicação..."
docker rmi www_crm-app > /dev/null 2>&1

# Compilar e iniciar os serviços (Maven será executado dentro do container)
echo "🔨 Compilando aplicação e iniciando serviços..."
echo "   📦 O Maven será executado automaticamente dentro do container Docker"
echo "   ⏳ Este processo pode demorar alguns minutos na primeira execução..."

docker-compose up --build -d

if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar os serviços. Verifique os logs do Docker."
    echo "🔍 Execute 'docker-compose logs app' para ver os logs detalhados."
    exit 1
fi

echo "⏳ Aguardando os serviços iniciarem..."
sleep 15

# Verificar se os serviços estão rodando
echo "🔍 Verificando status dos serviços..."
docker-compose ps

echo "🎉 CRM Application iniciado com sucesso!"
echo ""
echo "📱 Acesse a aplicação em:"
echo "   🌐 API: http://localhost:8080"
echo "   📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   🔧 Health Check: http://localhost:8080/actuator/health"
echo ""
echo "🗄️ Banco de dados PostgreSQL:"
echo "   📍 Host: localhost:5432"
echo "   💾 Database: crm_db"
echo "   👤 User: crm_user"
echo "   🔑 Password: crm_password"
echo ""
echo "📋 Para parar a aplicação: docker-compose down"
echo "📋 Para ver logs: docker-compose logs -f"