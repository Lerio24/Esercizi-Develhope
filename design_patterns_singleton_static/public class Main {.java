public class Main {

    // ---------------------- CLASSE USER ----------------------
    public static class User {
        private String nome;
        private int eta;

        // Getter e Setter
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getEta() {
            return eta;
        }

        public void setEta(int eta) {
            this.eta = eta;
        }

        // Metodo che stampa le informazioni
        public void stampaInfo() {
            System.out.println("Nome: " + nome + ", Età: " + eta);
        }
    }

    // ---------------------- MAIN ----------------------
    public static void main(String[] args) {

        // Primo oggetto User con valori di default
        User user1 = new User();
        System.out.println("User 1 (valori di default):");
        user1.stampaInfo();

        // Secondo oggetto User con valori modificati
        User user2 = new User();
        user2.setNome("Andrea");
        user2.setEta(28);

        System.out.println("User 2 (valori modificati):");
        user2.stampaInfo();
    }
}
