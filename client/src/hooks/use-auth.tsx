import { createContext, ReactNode, useContext } from "react";
import { useQuery, useMutation, UseMutationResult } from "@tanstack/react-query";
import { getQueryFn, queryClient } from "../lib/queryClient";
import { useToast } from "@/hooks/use-toast";

type User = {
    id: number;
    name: string; // Corresponde ao `name` da entidade Usuario
    username: string; // Corresponde ao `username`
    role: 'EMPLOYEE' | 'MANAGER' | 'ADMIN'; // Corresponde ao `role`
};

type LoginData = {
    username?: string;
    password?: string;
};

type AuthContextType = {
    user: User | null;
    isLoading: boolean;
    loginMutation: UseMutationResult<void, Error, LoginData>;
    logoutMutation: UseMutationResult<void, Error, void>;
};

export const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const { toast } = useToast();

    const {
        data: user,
        isLoading,
    } = useQuery<User | null, Error>({
        queryKey: ["/api/usuarios/me"], // Endpoint que retorna os dados do usuário logado
        queryFn: getQueryFn({ on401: "returnNull" }),
    });

    const loginMutation = useMutation<void, Error, LoginData>({
        mutationFn: async (credentials) => {
            const formData = new URLSearchParams();
            formData.append('username', credentials.username || '');
            formData.append('password', credentials.password || '');

            const res = await fetch("/api/login", {
                method: "POST",
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData,
            });

            if (!res.ok) {
                if (res.status === 401) {
                    throw new Error("Usuário ou senha inválidos.");
                }
                throw new Error("Ocorreu um erro ao tentar fazer login.");
            }
        },
        onSuccess: () => {
            toast({
                title: "Login bem-sucedido!",
                description: "Carregando seus dados...",
            });
            // Invalida a query para forçar uma nova busca dos dados do usuário agora autenticado
            queryClient.invalidateQueries({ queryKey: ["/api/usuarios/me"] });
        },
        onError: (error: Error) => {
            toast({
                title: "Falha no login",
                description: error.message,
                variant: "destructive",
            });
        },
    });

    const logoutMutation = useMutation<void, Error, void>({
        mutationFn: async () => {
            const res = await fetch("/api/logout", { method: "POST" });
            if (!res.ok) {
                throw new Error("Falha ao fazer logout.");
            }
        },
        onSuccess: () => {
            queryClient.setQueryData(["/api/usuarios/me"], null);
            // Redireciona para a página de login após o sucesso
            window.location.href = '/auth'; 
        },
        onError: (error: Error) => {
            toast({
                title: "Falha no logout",
                description: error.message,
                variant: "destructive",
            });
        },
    });

    const contextValue: AuthContextType = {
        user: user ?? null,
        isLoading,
        loginMutation,
        logoutMutation,
    };
    
    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}