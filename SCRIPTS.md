# 🚀 Scripts de Desenvolvimento

Este projeto possui diferentes scripts para facilitar o desenvolvimento:

## 📋 Scripts Disponíveis

### 1. `./start-dev.sh` - Modo Desenvolvimento
- **Sempre faz rebuild** do container
- Ideal para desenvolvimento ativo
- Não pergunta, sempre rebuilda
- **Uso**: `./start-dev.sh`

```bash
./start-dev.sh
```

### 2. `./start-with-logs.sh` - Modo Inteligente  
- **Detecta mudanças automaticamente**
- Pergunta se quer fazer rebuild quando detecta mudanças
- Mais rápido quando não há mudanças
- **Uso**: `./start-with-logs.sh` ou `./start-with-logs.sh --rebuild`

```bash
# Modo automático - detecta mudanças
./start-with-logs.sh

# Força rebuild sempre
./start-with-logs.sh --rebuild
```

### 3. `./start.sh` - Modo Produção
- Usa containers existentes
- Não faz rebuild automático
- Mais rápido para testes simples
- **Uso**: `./start.sh`

```bash
./start.sh
```

## 🔄 Quando usar cada script?

### Durante Desenvolvimento Ativo (mudanças frequentes no código)
```bash
./start-dev.sh
```
- ✅ Sempre rebuilda = sempre usa a versão mais recente
- ✅ Sem perguntas = mais rápido para ciclo dev
- ❌ Mais lento porque sempre rebuilda

### Para Testes/Mudanças Ocasionais
```bash
./start-with-logs.sh
```
- ✅ Detecta mudanças automaticamente
- ✅ Pergunta antes de rebuildar
- ✅ Rápido quando não há mudanças
- ✅ Controle manual sobre rebuild

### Para Testes Rápidos (sem mudanças)
```bash
./start.sh
```
- ✅ Muito rápido
- ❌ Não usa mudanças recentes no código

## 🛠️ Exemplos de Uso

### Cenário 1: Modificando controladores Java
```bash
# Você modificou AuthController.java
./start-dev.sh  # <- Sempre rebuilda, usa a nova versão
```

### Cenário 2: Testando API existente
```bash
# Só quer testar endpoints que já funcionam
./start.sh  # <- Rápido, usa container existente
```

### Cenário 3: Mudanças esporádicas
```bash
# Fez algumas mudanças, não tem certeza se quer rebuildar
./start-with-logs.sh  # <- Detecta mudanças e pergunta
```

## 🚨 Dicas Importantes

1. **Mudanças no código Java** = sempre precisam de rebuild
2. **Mudanças no `pom.xml`** = sempre precisam de rebuild  
3. **Apenas mudanças de configuração** = podem não precisar de rebuild
4. **Em caso de dúvida**, use `./start-dev.sh`

## 🔍 O que cada script faz internamente?

### `./start-dev.sh`
1. Para containers existentes
2. **Sempre** faz `docker-compose build --no-cache`
3. Inicia com logs visíveis

### `./start-with-logs.sh`
1. Verifica se há mudanças nos arquivos `.java` e `pom.xml`
2. Se há mudanças, pergunta se quer rebuildar
3. Se escolher "sim", faz rebuild
4. Inicia com logs visíveis

### `./start.sh`
1. Apenas executa `docker-compose up`
2. Usa containers/imagens existentes