# JavaPureAPI

Bem-vindo ao repositório `JavaPureAPI`, uma implementação simples e eficiente de uma API em Java puro. Este projeto é ideal para quem deseja aprender sobre Java e desenvolvimento de APIs sem o uso de frameworks adicionais.

## Características

- **Código Java Puro:** Focamos em utilizar apenas Java puro, sem frameworks externos.
- **Operações CRUD:** Implementações básicas de operações Create, Read, Update e Delete.
- **Handler como Controller:** Utilizamos um handler personalizado que atua como um controlador.
- **Inserção com Driver Oracle:** Demonstração de como realizar inserções usando o driver da Oracle.

## Pré-Requisitos

Para executar este projeto, você precisará do seguinte:
- Java JDK instalado.
- Docker para rodar a imagem do Oracle Database.

## Configuração do Docker com Oracle

Antes de rodar o `docker-compose`, é necessário configurar o login para baixar a imagem do Oracle Database. Siga estas etapas:

1. **Instalar o Docker:** Primeiro, instale o Docker a partir do [site oficial do Docker](https://www.docker.com/get-started).

2. **Configurar o Login no Docker Oracle:** Você precisará de uma conta na Oracle e liberar o acesso aos recursos. Se ainda não tiver, crie uma no site [Oracle](https://container-registry.oracle.com/). 

3. **Login via Terminal:** No terminal, execute o comando abaixo para fazer login:
   ```bash
   docker login container-registry.oracle.com 
   ```
   Insira suas credenciais do Docker Hub quando solicitado.

4. **Usar Docker-Compose:** No diretório do projeto, onde o arquivo `docker-compose.yml` está localizado, execute:
   ```bash
   docker-compose up
   ```
   Isso irá baixar e configurar automaticamente a imagem do Oracle especificada no `docker-compose.yml`.

5. **Verificar Containers:** Após a conclusão, verifique se os containers estão rodando corretamente com:
   ```bash
   docker ps
   ```

6. **Conectar com a API:** Atualize as configurações de conexão da API para apontar para o banco de dados Oracle no Docker.

## Como Executar

1. **Clone o Repositório:** Faça um clone do repositório para sua máquina local.
2. **Configure o Ambiente:** Siga as instruções acima para configurar o Docker com Oracle.
3. **Execute a API:** Utilize sua IDE Java preferida ou linha de comando para executar a API.

## Contribuições

Sua contribuição é muito bem-vinda! Se você tem sugestões de melhorias ou novas funcionalidades, fique à vontade para criar um pull request.
