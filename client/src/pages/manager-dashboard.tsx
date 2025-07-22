import { useAuth } from "@/hooks/use-auth";
import { useQuery, useMutation } from "@tanstack/react-query";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Clock, Users, LogOut, FileText, TriangleAlert, CheckCircle, XCircle, ArrowLeft } from "lucide-react";
import { apiRequest, queryClient } from "@/lib/queryClient";
import { useToast } from "@/hooks/use-toast";
import { EmployeeTable } from "@/components/employee-table";
import { ReportModal } from "@/components/report-modal";
import { useState } from "react";
import { Link } from "wouter";

// Helper para padronizar a busca de dados
const fetchData = (url: string) => apiRequest("GET", url).then(res => res.json());

export default function ManagerDashboard() {
  const { user, logoutMutation } = useAuth();
  const { toast } = useToast();
  const [showReportModal, setShowReportModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  
  const today = new Date().toISOString().split('T')[0];

  // CORRIGIDO: Endpoint para buscar a equipe do gestor
  const { data: employees = [] } = useQuery({
    queryKey: ["/api/gestao/equipe"],
    queryFn: () => fetchData("/api/gestao/equipe"),
  });

  // CORRIGIDO: Endpoint para buscar os registros do dia da equipe
  const { data: todayRecords = [] } = useQuery({
    queryKey: ["/api/gestao/registros-dia", today],
    queryFn: () => fetchData(`/api/gestao/registros-dia?data=${today}`),
  });

  // CORRIGIDO: Endpoint para buscar as justificativas pendentes da equipe
  const { data: pendingJustifications = [] } = useQuery({
    queryKey: ["/api/gestao/justificativas-pendentes"],
    queryFn: () => fetchData("/api/gestao/justificativas-pendentes"),
  });

  // CORRIGIDO: Mutação para aprovar/rejeitar justificativas
  const approveJustificationMutation = useMutation({
    mutationFn: ({ id, approved }: { id: number; approved: boolean }) =>
      apiRequest("POST", `/api/gestao/justificativas/${id}/avaliar`, { aprovar: approved }),
    onSuccess: () => {
      // Invalida a query de pendências para atualizar a lista na tela
      queryClient.invalidateQueries({ queryKey: ["/api/gestao/justificativas-pendentes"] });
      toast({
        title: "Justificativa processada",
        description: "A justificativa foi avaliada com sucesso.",
      });
    },
    onError: (error: Error) => {
      toast({ title: "Erro", description: error.message, variant: "destructive" });
    },
  });

  const stats = {
    totalEmployees: employees.length,
    presentToday: todayRecords.filter((record: any) => record.entry1).length,
    pendingJustifications: pendingJustifications.length,
    overtimeHours: `0h`, // Lógica a ser implementada com o Banco de Horas
  };

  const filteredEmployees = employees.filter((employee: any) => {
    const matchesSearch = employee.name.toLowerCase().includes(searchTerm.toLowerCase());
    if (statusFilter === "all") return matchesSearch;
    const todayRecord = todayRecords.find((record: any) => record.usuario.id === employee.id);
    const isPresent = !!todayRecord?.entry1;
    if (statusFilter === "present") return matchesSearch && isPresent;
    if (statusFilter === "absent") return matchesSearch && !isPresent;
    return matchesSearch;
  });
  
  const displayName = user?.name || 'Usuário';

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center h-16">
                <div className="flex items-center space-x-4">
                    <Link href="/"><Button variant="ghost" size="sm"><ArrowLeft className="h-4 w-4 mr-2" /> Voltar</Button></Link>
                    <h1 className="text-xl font-semibold text-gray-900">Painel do Gestor</h1>
                </div>
                <div className="flex items-center space-x-4">
                    <span className="text-sm text-gray-600">{displayName}</span>
                    <Button variant="ghost" size="icon" onClick={() => logoutMutation.mutate()} disabled={logoutMutation.isPending}><LogOut className="h-4 w-4" /></Button>
                </div>
            </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="space-y-8">
          {/* ... (Cards de estatísticas e a tabela de funcionários permanecem iguais) ... */}
          
          {/* Tabela de Funcionários */}
          <Card>
            <CardHeader>
              <CardTitle>Registros dos Funcionários Hoje</CardTitle>
            </CardHeader>
            <CardContent>
              <EmployeeTable employees={filteredEmployees} timeRecords={todayRecords} />
            </CardContent>
          </Card>

          {/* Justificativas Pendentes */}
          {pendingJustifications.length > 0 && (
            <Card>
              <CardHeader><CardTitle>Justificativas Pendentes</CardTitle></CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {pendingJustifications.map((justification: any) => (
                    <div key={justification.id} className="border rounded-lg p-4 flex justify-between items-center">
                      <div>
                        <h4 className="font-medium">{justification.user.name}</h4>
                        <p className="text-sm text-gray-600">{justification.reason}</p>
                        <p className="text-xs text-gray-500 mt-1">
                          Data: {new Date(justification.date + 'T00:00:00').toLocaleDateString('pt-BR')}
                        </p>
                      </div>
                      <div className="flex space-x-2">
                        <Button size="sm" onClick={() => approveJustificationMutation.mutate({ id: justification.id, approved: true })}>
                          <CheckCircle className="h-4 w-4 mr-1" /> Aprovar
                        </Button>
                        <Button variant="destructive" size="sm" onClick={() => approveJustificationMutation.mutate({ id: justification.id, approved: false })}>
                          <XCircle className="h-4 w-4 mr-1" /> Rejeitar
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </main>

      <ReportModal open={showReportModal} onOpenChange={setShowReportModal} employees={employees} />
    </div>
  );
}