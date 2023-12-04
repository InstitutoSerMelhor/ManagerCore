<h2> Instruções </h2>

<details>
  <summary><strong> Rodando projeto </strong></summary>  
  <br/>
  
1. Clone o repositório com o comando:
  - `git clone git@github.com:InstitutoSerMelhor/ManagerCore.git`;
    - Entre na pasta do repositório:
      - `cd ManagerCore`     

2. Inicie a aplicação com o comando `docker-compose up --build -d`.
  - Caso queria parar os containers `docker-compose down`

3. Dentro do diretório raiz do projeto:
    - Instale as dependências `mvn install`.
    - E inicie a aplicação `mvn spring-boot:run`

3. Acesse a aplicação usando essa url `http://localhost:8080/swagger-ui/index.html`.

</details>

<details>
  <summary><strong> Rodando testes </strong></summary>
  <br/>
  
  - Rodando testes(você deve estar na pasta raiz) - `mvn test`.

</details>

<details>
  <summary><strong> Rodando cobertura de testes </strong></summary>
  <br/>

- Rode na raiz do projeto `mvn clean verify`.
- Vá a pasta `/target/site` que foi gerada.
- Abra o arquivo `index.html` no navegador e já poderá ver a cobertura de testes.

</details>

<details>
  <summary><strong>Acordos para manter a organização</strong></summary>
  <br/>
  
  - Não faça pushes para na branch main, ou seja, sempre trabalhe em uma nova branch, ver seção _1 - Antes de começar a desenvolver_.
  - Siga os padrões de branch e commit, ver seção _1 - Antes de começar a desenvolver_.
  - Tá com dificulade? Peça ajuda!

</details>

<h3> Como começar e entregar uma tarefa? </h3>

<details>
  <summary>
    <strong>1 - Antes de começar a desenvolver</strong>
  </summary><br>

  1. Crie uma branch a partir da branch `main`

  - Verifique se você está na branch `main`
    - Exemplo: `git branch`
  - Se não estiver, mude para a branch `main`
    - Exemplo: `git checkout main`
    
  - Agora crie uma branch onde você vai submeter os `commits` do seu projeto
    - Você deve criar uma branch no seguinte formato: `tipo-de-modificação/titulo-da-modificação`
    - Exemplo: `git checkout -b feat/add-endpoint-delete-user`

  2. Adicione as mudanças ao _stage_ do Git e faça um `commit`

  - Verifique que as mudanças ainda não estão no _stage_
    - Exemplo: `git status`

  - Adicione o novo arquivo ao _stage_ do Git
      - Exemplo:
        - `git add .` (adicionando todas as mudanças - _que estavam em vermelho_ - ao stage do Git)
        - `git status` (deve aparecer listado todas as mudanças agora em verde)

  - Faça o `commit` inicial
      - Exemplo:
        - `git commit -m ''` (fazendo o primeiro commit)
        - `git status` (deve aparecer uma mensagem tipo _nothing to commit_ )

  3. Adicione a sua branch com o novo `commit` ao repositório remoto

  - Usando o exemplo anterior: `git push -u origin feat/add-endpoint-delete-userp`

  4. Crie um novo `Pull Request` _(PR)_

  - Vá até a página de _Pull Requests_ do [repositório no GitHub](https://github.com/InstitutoSerMelhor/ManagerCore/pulls)
  - Clique no botão verde _"New pull request"_
  - Clique na caixa de seleção _"Compare"_ e escolha a sua branch **com atenção**
  - Coloque um título para a sua _Pull Request_
    - Exemplo: _"Add endpoint para deletar um usuário"_
  - Clique no botão verde _"Create pull request"_
  - Adicione uma descrição para o _Pull Request_ e clique no botão verde _"Create pull request"_
  - Volte até a [página de _Pull Requests_ do repositório](https://github.com/InstitutoSerMelhor/ManagerCore/pulls) e confira que o seu _Pull Request_ está criado

</details>

<details>
  <summary>
    <strong>2 - Durante o desenvolvimento</strong>
  </summary><br>

  - Faça `commits` das alterações que você fizer no código regularmente.

  - Lembre-se de sempre após um (ou alguns) `commits` atualizar o repositório remoto.

  - Os comandos que você utilizará com mais frequência são:
    1. `git status` _(para verificar o que está em vermelho - fora do stage - e o que está em verde - no stage)_
    2. `git add` _(para adicionar arquivos ao stage do Git)_
    3. `git commit` _(para criar um commit com os arquivos que estão no stage do Git)_
    4. `git push -u nome-da-branch` _(para enviar o commit para o repositório remoto na primeira vez que fizer o `push` de uma nova branch)_
    5. `git push` _(para enviar o commit para o repositório remoto após o passo anterior)_

</details>

<details>
  <summary>
    <strong>3 - Depois de terminar o desenvolvimento</strong>
  </summary><br>

  Para sinalizar que o seu projeto está pronto para o _"Code Review"_, faça o seguinte:

  * Vá até a página **DO SEU** _Pull Request_, adicione a label de _"code-review"_ e marque seus colegas:

    * No menu à direita, clique no _link_ **"Labels"** e escolha a _label_ **code-review**;

    * No menu à direita, clique no _link_ **"Assignees"** e escolha **o seu usuário**;

    * No menu à direita, clique no _link_ **"Reviewers"** e digite o username de algum colaborador do projeto``, exemplo `abnerferreiradesousa`.

</details>
