public class Customer {
    private int id;
    private int serialId;
    private String name;
    private String gender;
    private String country;
    private int grade;

    public Customer(int id, int serialId, String name,
                    String gender, String country, int grade) {
        this.id = id;
        this.serialId = serialId;
        this.name = name;
        this.gender = gender;
        this.country = country;
        this.grade = grade;
    }

    void setSerialId(int serialId) {    this.serialId = serialId;   }
    int getSerialId() { return this.serialId;   }

    String getName() {  return this.name;   }
    String getGender()  {   return this.gender; }
    String getCountry() {   return this.country;    }
    int getGrade()  {   return this.grade;   }

    public String toString() {
        return String.format("이름: %s, 성별: %s, 국가: %s, 고객 등급: %d" , name, gender, country, grade);
    }
}
