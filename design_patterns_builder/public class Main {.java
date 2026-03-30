public class Main {

    // ---------------------- PERSON ----------------------
    public static class Person {
        private String firstName;
        private String lastName;
        private int age;
        private String address;

        // Costruttore che accetta il Builder
        public Person(Builder builder) {
            this.firstName = builder.firstName;
            this.lastName = builder.lastName;
            this.age = builder.age;
            this.address = builder.address;
        }

        // Getter e Setter
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        @Override
        public String toString() {
            return "Person{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    // ---------------------- BUILDER ----------------------
    public static class Builder {
        private final String firstName;   // obbligatorio
        private final String lastName;    // obbligatorio
        private int age;                  // opzionale
        private String address;           // opzionale

        // Costruttore con i campi obbligatori
        public Builder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        // Metodi opzionali
        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        // Metodo finale che crea la Person
        public Person build() {
            return new Person(this);
        }
    }

    // ---------------------- MAIN ----------------------
    public static void main(String[] args) {

        Person p1 = new Builder("Mario", "Rossi")
                .age(30)
                .address("Via Roma 10, Bologna")
                .build();

        Person p2 = new Builder("Luca", "Bianchi")
                .build(); // nessun campo opzionale

        System.out.println(p1);
        System.out.println(p2);
    }
}
