import { useAuth } from "@/hooks/use-auth";
import { useQuery, useMutation } from "@tanstack/react-query";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Clock, Users, LogOut, Plus } from "lucide-react";
import { queryClient } from "@/lib/queryClient";
import { useToast } from "@/hooks/use-toast";
import { TimeRegistrationGrid } from "@/components/time-registration-grid";
import { JustificationModal } from "@/components/justification-modal";
import { useState } from "react";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Link } from "wouter";

// Helper para simplificar as chamadas fetch
const fetchData = async (url: string) => {
    const res = await fetch(url, { credentials: 'include' });
    if (!res.ok) {
        throw new Error(`Network response was not ok for ${url}`);
    }
    // Retorna null se o corpo estiver vazio, para evitar erro de JSON.parse()
    const text = await res.text();
    return text ? JSON.parse(text) : null;
};

export default function HomePage() {
  const { user, logoutMutation } = useAuth();
  const { toast } = useToast();
  const [showJustificationModal, setShowJustificationModal] = useState(false);
  const [selectedMonth, setSelectedMonth] = useState(() => {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
  });

  const { data: todayRecord } = useQuery({
    queryKey: ["/api/ponto/hoje"],
    queryFn: () => fetchData("/api/ponto/hoje"),
    enabled: !!user && user.role === "EMPLOYEE",
  });

  // ... (outras queries permanecem iguais)

  const timeRegistrationMutation = useMutation({
    mutationFn: async () => {
        const res = await fetch("/api/ponto/registrar", { 
            method: "POST", 
            credentials: 'include' 
        });

        if (!res.ok) {
            const errorData = await res.json();
            throw new Error(errorData.message || "Ocorreu um erro desconhecido.");
        }
        return res.json();
    },
    onSuccess: () => {
      // CORREÇÃO: Invalida a query correta para forçar a atualização dos dados do dia
      queryClient.invalidateQueries({ queryKey: ["/api/ponto/hoje"] });
      toast({
        title: "Registro realizado",
        description: "Seu horário foi registrado com sucesso.",
      });
    },
    onError: (error: Error) => {
      toast({
        title: "Erro no registro",
        description: error.message,
        variant: "destructive",
      });
    },
  });

  // ... (o resto do componente permanece o mesmo)
  const getNextRegistrationType = () => {
    if (!todayRecord) return "Entrada 1";
    if (!todayRecord.exit1) return "Saída 1";
    if (!todayRecord.entry2) return "Entrada 2";
    if (!todayRecord.exit2) return "Saída 2";
    return "Completo";
  };

  const canRegister = getNextRegistrationType() !== "Completo";
  const displayName = user?.name || 'Usuário';

  return (
    // O JSX do return permanece o mesmo da correção anterior
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-4">
              <h1 className="text-xl font-semibold text-gray-900">Sistema de Ponto</h1>
            </div>
            <div className="flex items-center space-x-4">
              <div className="hidden sm:flex items-center space-x-2">
                <span className="text-sm text-gray-600">{displayName}</span>
                <Badge variant={user?.role === "MANAGER" ? "default" : "secondary"}>
                  {user?.role === "MANAGER" ? "Gestor" : user?.role === "ADMIN" ? "Admin" : "Funcionário"}
                </Badge>
              </div>
              {(user?.role === "MANAGER" || user?.role === "ADMIN") && (
                <Link href={user.role === "ADMIN" ? "/admin" : "/manager"}>
                  <Button variant="outline" size="sm"><Users className="h-4 w-4 mr-2" /> Painel</Button>
                </Link>
              )}
              <Button variant="ghost" size="icon" onClick={() => logoutMutation.mutate()} disabled={logoutMutation.isPending}>
                <LogOut className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="space-y-8">
          {/* ... */}
          {user?.role === "EMPLOYEE" && (
            <Card>
              <CardContent className="p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-6">Suas Marcações do Dia</h3>
                <TimeRegistrationGrid timeRecord={todayRecord} />
                <div className="text-center mt-8">
                  <Button
                    size="lg"
                    onClick={() => timeRegistrationMutation.mutate()}
                    disabled={!canRegister || timeRegistrationMutation.isPending}
                    className="px-8 py-4 text-lg"
                  >
                    <Clock className="mr-3 h-5 w-5" />
                    {canRegister ? `Registrar ${getNextRegistrationType()}` : "Registros Completos"}
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}
          {/* ... */}
        </div>
      </main>
      
      <JustificationModal open={showJustificationModal} onOpenChange={setShowJustificationModal} />
    </div>
  );
}