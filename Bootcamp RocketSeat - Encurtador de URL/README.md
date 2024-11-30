
## Participação em Evento

Este projeto foi desenvolvido como parte do Curso Gratuito de Java da [@rocketseat](https://github.com/rocketseat)  , que aconteceu entre 18/11/2024 e 22/11/2024, com 5 horas de duração.



### Conteúdos Abordados:
* Desenvolvimento de aplicação serverless com Java e Maven para redirecionamento de URLs.

* Integração com AWS S3 para criação e gerenciamento de buckets.

* Exposição de endpoints via API Gateway.

* Uso de AWS Lambda para processamento serverless.

* Manipulação de dados em JSON com Jackson.

* Testes de API utilizando Insomnia.

### Tecnologias
* **Java:** _Linguagem de programação utilizada._

* **AWS Lambda:** _Executa o código em resposta a eventos._

* **Amazon S3:** _Armazena as URLs encurtadas._

* **Jackson:** _Biblioteca para manipulação de JSON._

* **Maven:** _Gerenciador de dependências e construção do projeto._

* **Insomnia:** _Ferramenta para testar e interagir com APIs._

### Funcionalidades

* **Criação de URL Curta:** _Gera um código curto único para cada URL original._
* **Validade da URL:** _Permite definir um tempo de expiração para a URL curta._
* **Redirecionamento:** _Redireciona o usuário para a URL original se a URL curta for válida._
* **Tratamento de Erros:** _Retorna mensagens de erro apropriadas se a URL estiver expirada ou se houver problemas na requisição._

### Como Funciona
1. **Criação de URL Curta:**

* O usuário envia uma requisição com um JSON contendo a URL original e o tempo de expiração.

* O sistema gera um código curto e armazena a URL original e o tempo de expiração no S3.
2. **Redirecionamento:**
* Quando a URL curta é acessada, o sistema busca os dados no S3.

* Se a URL ainda for válida, o usuário é redirecionado para a URL original. Caso contrário, uma mensagem de erro é retornada.

## Instalação
Para instalar e executar o projeto, siga os passos abaixo:
1. **Clone o repositório:**

git clone https://github.com/seu-usuario/encurtador-url.git
cd encurtador-url

2. **Configure o Maven:**
* Certifique-se de ter o Maven instalado em sua máquina.

* Execute o comando para compilar o projeto:
  _mvn clean package_

3. **Implante na AWS:**
* Faça o upload do arquivo JAR gerado para o AWS Lambda.

* Configure as permissões necessárias para acessar o S3.

### Uso
Para encurtar uma URL, envie uma requisição POST para a função Lambda com o seguinte corpo JSON:

~~~JSON 
{
    "originalUrl": "https://www.exemplo.com", 

    "expirationTime": "3600"
}
~~~

_originalUrl: A URL que você deseja encurtar._

_expirationTime: O tempo em segundos até que a URL expire._

Para acessar a URL encurtada, basta usar o código curto gerado.

### Testando a API
Você pode usar o **Insomnia** para testar a API. Crie uma nova requisição POST e insira a URL do endpoint da função Lambda, juntamente com o corpo JSON mencionado acima.


### Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir uma issue ou enviar um pull request. Para contribuir, siga estas etapas:

1. Fork o repositório.

2. Crie uma nova branch (git checkout -b feature/nome-da-sua-feature).

3. Faça suas alterações e commit (git commit -m 'Adiciona nova funcionalidade').

4. Envie para o repositório remoto (git push origin feature/nome-da-sua-feature).

5. Abra um Pull Request.

### Licença

Este projeto está licenciado sob a MIT License. Veja o arquivo LICENSE para mais detalhes.
