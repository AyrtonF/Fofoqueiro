# **Guia de Padronização e Boas Práticas \- Frontend**

## **Visão Geral**

Este documento descreve os padrões, princípios arquiteturais e boas práticas para o desenvolvimento de aplicações Frontend utilizando **Next.js (App Router)**, **TypeScript** e **Tailwind CSS**. O objetivo é garantir um código sustentável, escalável, testável e de alta performance, espelhando a robustez do nosso ecossistema de Backend.

## **1\. Princípios Fundamentais**

### **1.1 SOLID no React**

Os princípios SOLID são aplicados ao Frontend para evitar componentes "monstruosos" e lógica acoplada:

* **S — Single Responsibility Principle (SRP):** Um componente deve fazer apenas uma coisa. Se um componente lida com busca de dados, formatação e renderização, ele deve ser dividido.  
* **O — Open/Closed Principle (OCP):** Componentes devem ser abertos para extensão (via props/composition) mas fechados para modificação.  
* **L — Liskov Substitution Principle (LSP):** Componentes base (ex: um Button customizado) devem aceitar todas as propriedades do elemento HTML nativo que substituem.  
* **I — Interface Segregation Principle (ISP):** Não passe objetos gigantes para um componente se ele precisa apenas de uma propriedade. Use tipos específicos.  
* **D — Dependency Inversion Principle (DIP):** Dependa de abstrações (hooks de interface) e não de implementações concretas de chamadas de API dentro dos componentes.

### **1.2 Clean Architecture (Frontend)**

Dividimos a aplicação em camadas para isolar a lógica de negócio da UI:

1. **Presentation Layer:** Componentes React (Server e Client).  
2. **Application Layer:** Hooks customizados que orquestram estados e chamadas.  
3. **Domain Layer:** Tipos (Interfaces), Enums e lógica de validação pura.  
4. **Infrastructure/Service Layer:** Clientes de API (Axios/Fetch), adaptadores de persistência.

## **2\. Estrutura de Pastas (Next.js App Router)**

src/  
├── app/                  \# Routes, Layouts e Server Components  
├── components/           \# Componentes UI reutilizáveis  
│   ├── ui/               \# Componentes atômicos (Button, Input) \- Shadcn/ui  
│   ├── common/           \# Componentes globais (Navbar, Footer)  
│   └── modules/          \# Componentes complexos divididos por domínio/funcionalidade  
├── hooks/                \# Custom hooks (Lógica de aplicação)  
├── services/             \# Chamadas de API e integrações externas  
├── domain/               \# Core de negócio: Interfaces, Enums, Utils puras  
├── store/                \# Estado global (Zustand, Context API)  
└── styles/               \# Configurações globais de CSS/Tailwind

## **3\. Componentização e UI**

### **3.1 Nomenclatura**

* **Arquivos de Componente:** PascalCase (Button.tsx, UserCard.tsx).  
* **Pastas de Módulo:** kebab-case (user-profile, auth-form).  
* **Variáveis e Funções:** camelCase (isLoaded, handleClick).  
* **Arquivos de Estilo/Config:** kebab-case (tailwind.config.ts).

### **3.2 Componentes de Servidor vs. Cliente**

* **Server Components (Padrão):** Use para buscar dados iniciais e renderizar conteúdo estático. Maximiza a performance e o SEO.  
* **Client Components ('use client'):** Use apenas quando houver interatividade (useState, useEffect, eventos de clique).  
* **Regra:** Mantenha as diretivas 'use client' o mais baixo possível na árvore de componentes.

### **3.3 Tailwind CSS e Estilização**

* **Utility-First:** Use as classes do Tailwind diretamente.  
* **Condicionais:** Use a biblioteca clsx ou tailwind-merge para classes dinâmicas.  
* **Design System:** Defina cores, espaçamentos e fontes no tailwind.config.ts. Evite "magic numbers" (ex: w-\[347px\]).  
* **Exemplo de componente limpo:**

import { cn } from "@/lib/utils";

interface ButtonProps extends React.ButtonHTMLAttributes\<HTMLButtonElement\> {  
  variant?: 'primary' | 'secondary';  
}

export function Button({ variant \= 'primary', className, ...props }: ButtonProps) {  
  return (  
    \<button   
      className={cn(  
        "px-4 py-2 rounded-md transition-colors",  
        variant \=== 'primary' ? "bg-blue-500 text-white hover:bg-blue-600" : "bg-gray-200",  
        className  
      )}  
      {...props}  
    /\>  
  );  
}

## **4\. Camada de Aplicação e Dados**

### **4.1 Gerenciamento de Estado de Servidor**

* **TanStack Query (React Query):** Obrigatório para cache, sincronização e estados de loading/error de requisições no lado do cliente.  
* **Server Actions:** Use para mutações de dados diretamente do formulário para o banco/API.

### **4.2 Camada de Service (API Adapter)**

Encapsule chamadas de API para facilitar a manutenção e troca de biblioteca.

// services/api-client.ts  
export const api \= axios.create({ baseURL: process.env.NEXT\_PUBLIC\_API\_URL });

// services/user-service.ts  
export const userService \= {  
  getById: (id: string) \=\> api.get\<User\>(\`/users/${id}\`).then(res \=\> res.data),  
  update: (id: string, data: UserUpdateDTO) \=\> api.put(\`/users/${id}\`, data)  
};

### **4.3 Hooks como "Controllers"**

Não deixe lógica de negócio dentro do arquivo .tsx. Mova para um hook.

// hooks/use-user-profile.ts  
export function useUserProfile(userId: string) {  
  const { data, isLoading } \= useQuery({  
    queryKey: \['user', userId\],  
    queryFn: () \=\> userService.getById(userId)  
  });

  const formattedDate \= data ? formatDate(data.createdAt) : '';

  return { user: data, isLoading, formattedDate };  
}

## **5\. TypeScript e Tipagem Correta**

* **Evite any:** O uso de any é terminantemente proibido. Use unknown se o tipo for incerto.  
* **Interfaces vs Types:** Use interface para objetos e definições que podem ser estendidas; use type para uniões e tipos primitivos.  
* **Prop Types:** Sempre defina as props dos componentes.  
* **Zod para Validação:** Use a biblioteca Zod para validar schemas de formulários e respostas de API.

## **6\. Boas Práticas e Clean Code**

### **6.1 Código Limpo**

* **Comentários:** Comente apenas o "porquê", nunca o "o quê". Se o código é complexo demais para entender sem comentários, simplifique-o.  
* **Funções Pequenas:** Funções não devem passar de 20-30 linhas.  
* **Early Return:** Evite aninhamento excessivo de if.

// Bom  
if (\!user) return \<Loading /\>;  
if (hasError) return \<ErrorMessage /\>;  
return \<UserProfile user={user} /\>;

### **6.2 Acessibilidade (a11y)**

* Use tags semânticas (\<main\>, \<section\>, \<article\>, \<nav\>).  
* Garanta que todos os elementos interativos sejam acessíveis via teclado (tabIndex).  
* Use atributos aria-label onde o contexto visual não é óbvio.

## **7\. Estratégia de Testes**

1. **Unitários (Vitest \+ Testing Library):** Para funções utilitárias e componentes de UI isolados (ex: Button, Inputs).  
2. **Integração:** Testar a interação entre hooks e múltiplos componentes.  
3. **E2E (Playwright):** Fluxos críticos (Login, Checkout, Cadastro).

## **8\. Checklist de Desenvolvimento**

* \[ \] O componente segue o SRP (faz apenas uma coisa)?  
* \[ \] Lógica de estado complexa foi extraída para um Hook?  
* \[ \] O componente é responsivo (Mobile-first com Tailwind)?  
* \[ \] As props e retornos de funções estão devidamente tipados?  
* \[ \] Foi feito o tratamento de estados de erro e loading?  
* \[ \] O código passa no ESLint e Prettier?  
* \[ \] Variáveis de ambiente estão no .env.example?

## **9\. Conclusão**

Seguir este padrão garante que o projeto frontend mantenha a mesma qualidade e previsibilidade que o backend. A separação clara entre a camada de visualização e a lógica de negócio permite que a aplicação cresça de forma organizada e facilite a entrada de novos desenvolvedores no time.