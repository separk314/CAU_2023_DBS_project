import java.util.*;

public class Controller {
    static int serialId = 0;

    static Scanner scanner = new Scanner(System.in);
    static Bitmap genderIndex = new Bitmap("genderIndex");
    static Bitmap countryIndex = new Bitmap("countryIndex");
    static Bitmap gradeIndex = new Bitmap("gradeIndex");

    static void addTuple() {
        System.out.println("< 생성할 고객 정보를 입력해주세요 >");
        System.out.print("고객 이름을 입력하세요: ");
        String name = scanner.next();

        String gender = "";
        while (true) {
            System.out.println("성별을 선택하세요: 남자는 1, 여자는 2를 입력");
            int genderInt = scanner.nextInt();
            if (genderInt == 1) {
                gender = "m";
                break;
            } else if (genderInt == 2) {
                gender = "f";
                break;
            } else {
                System.out.println("다시 입력해주세요.");
            }
        }

        System.out.print("고객이 거주 중인 나라를 입력하세요: ");
        String country = scanner.next();

        int grade = 0;
        while (true) {
            System.out.println("고객 등급을 입력하세요: 1~4 중에서 입력");
            int gradeInt = scanner.nextInt();
            if (gradeInt >= 1 && gradeInt <= 4) {
                grade = gradeInt;
                break;
            } else {
                System.out.println("다시 입력해주세요.");
            }
        }

        Customer customer = new Customer(serialId, serialId, name, gender, country, grade);
        System.out.println("< 고객 생성 완료 >");
        System.out.println(customer);

        genderIndex.add(gender, serialId);
        countryIndex.add(country, serialId);
        gradeIndex.add(String.valueOf(grade), serialId);

        serialId++;
    }
    static void multipleKeyQuery() {
        Map<Bitmap, String> queries = new HashMap<>();
        String genderQuery = "";
        String countryQuery = "";
        String gradeQuery = "";

        // Gender
        System.out.println("gender에 대한 질의를 생성하시겠습니까?");
        System.out.println("gender에 대한 질의 생성: 1 입력");
        int result = scanner.nextInt();

        if (result == 1) {
            System.out.println("SELECT * \nFROM CUSTOMER \nWHERE gender=?");
            System.out.println("gender='m'인 질의를 선택: 1 입력");
            System.out.println("gender='f'인 질의를 선택: 2 입력");
            int genderResult = scanner.nextInt();

            if (genderResult == 1) {
                queries.put(genderIndex, "m");
                genderQuery = "gender='m'";
            } else if (genderResult == 2) {
                queries.put(genderIndex, "f");
                genderQuery = "gender='f'";
            } else {
                System.out.println("< Invalid input >");
                return;
            }
        } else {
            System.out.println("gender에 대한 질의를 생성하지 않음.");
        }

        // Country
        System.out.println("country에 대한 질의를 생성하시겠습니까?");
        System.out.println("country에 대한 질의 생성: 1 입력");
        result = scanner.nextInt();

        if (result == 1) {
            System.out.println("SELECT * \nFROM CUSTOMER \nWHERE country=?");
            System.out.print("WHERE절 안에 들어갈 country를 입력하세요: ");
            String countryResult = scanner.next();

            queries.put(countryIndex, countryResult);
            countryQuery = "country="+countryResult;
        } else {
            System.out.println("gender에 대한 질의를 생성하지 않음.");
        }

        // Grade
        System.out.println("grade에 대한 질의를 생성하시겠습니까?");
        System.out.println("grade에 대한 질의 생성: 1 입력");
        result = scanner.nextInt();

        if (result == 1) {
            System.out.println("SELECT * \nFROM CUSTOMER \nWHERE grade=?");
            System.out.print("WHERE절 안에 들어갈 grade를 입력하세요: ");
            int gradeResult = scanner.nextInt();

            queries.put(gradeIndex, String.valueOf(gradeResult));
            gradeQuery = "grade="+gradeResult;
        } else {
            System.out.println("gender에 대한 질의를 생성하지 않음.");
        }

        // Result
        Bitmap MultiKeyresult = genderIndex.processMultipleKeyQuery(queries);
        if (MultiKeyresult == null) {
            System.out.println("< 입력한 Query가 없습니다. >");
            return;
        }

        System.out.println("< Result >");
        System.out.print("SELECT * \nFROM CUSTOMER \nWHERE ");
        if (genderQuery != "") {
            System.out.print(gradeQuery);
            if (countryQuery != "" || gradeQuery != "") System.out.print(" AND ");
        }
        if (countryQuery != "") {
            System.out.print(countryQuery);
            if (gradeQuery != "") System.out.print("AND");
        }
        if (gradeQuery != "") {
            System.out.print(gradeQuery);
        }

        System.out.println("\n"+ MultiKeyresult.getIndex().get("queryResult"));
    }

    static void countQuery() {
        while (true) {
            System.out.println("집계 함수를 실행할 column을 선택하세요.");
            System.out.println("1. gender(성별)");
            System.out.println("2. country(나라)");
            System.out.println("3. grade(고객 등급)");
            int result = scanner.nextInt();

            if (result == 1) {
                System.out.println("gender key값을 입력하세요: 남자는 1, 여자는 2를 선택");
                int genderScan = scanner.nextInt();
                if (genderScan == 1) {
                    System.out.println(genderIndex.processCountQuery("m"));
                } else if (genderScan == 2) {
                    System.out.println(genderIndex.processCountQuery("f"));
                } else {
                    System.out.println("< Invalid input >");
                }
                break;

            } else if (result == 2) {
                System.out.print("country key값을 입력하세요: ");
                String countryScan = scanner.next();
                System.out.println(countryIndex.processCountQuery(countryScan));
                break;

            } else if (result == 3) {
                System.out.println("grade key값을 입력하세요: 1~4 중에서 선택");
                int gradeScan = scanner.nextInt();
                if (gradeScan >= 1 && gradeScan <= 4) {
                    System.out.println(gradeIndex.processCountQuery(String.valueOf(gradeScan)));
                } else {
                    System.out.println("< Invalid input >");
                }
                break;

            } else {
                System.out.println("다시 입력해주세요.");
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n< 고객 관리 Application >");
            System.out.println("1. record 삽입");
            System.out.println("2. DB에 있는 bitmap index 확인");
            System.out.println("3. multiple-key 질의");
            System.out.println("4. 집계함수 count(*) 처리");
            System.out.println("5. 종료");
            System.out.print("숫자를 입력해주세요: ");

            int result = scanner.nextInt();

            if (result == 1) {
                addTuple();

            } else if (result == 2) {
                System.out.println("gender, country, grade index가 있습니다.");

            } else if (result == 3) {
                multipleKeyQuery();

            } else if (result == 4) {
                countQuery();

            } else if (result == 5) {
                System.out.println("< 종료 >");
                break;
            } else {
                System.out.println("\n<Invalid Input> 다시 입력해주세요");
            }
        }
    }
}
//
//        queryMap.put(countryIndex, "Japan");
//
//        Bitmap result = genderIndex.processMultipleKeyQuery(queryMap);
//        if (result != null) {
//            System.out.println("Result: " + result.getIndex().get("queryResult"));
//        } else {
//            System.out.println("No result found.");
//        }
//    }
//}
//
