import java.util.*;

public class Controller {
    static int serialId = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bitmap genderIndex = new Bitmap("genderIndex");
        Bitmap countryIndex = new Bitmap("countryIndex");
        Bitmap gradeIndex = new Bitmap("gradeIndex");

        while (true) {
            System.out.println("< 고객 관리 Application >");
            System.out.println("1. record 삽입");
            System.out.println("2. DB에 있는 bitmap index 확인");
            System.out.println("3. multiple-key 질의");
            System.out.println("4. 집계함수 count(*) 처리");
            System.out.println("5. 종료");
            System.out.print("숫자를 입력해주세요: ");

            int result = scanner.nextInt();

            if (result == 1) {
                System.out.println("< 생성할 고객 정보를 입력해주세요 >");
                System.out.print("고객 이름을 입력하세요: ");
                String name = scanner.next();

                int gender = 0;
                while (true) {
                    System.out.println("성별을 선택하세요: 남자는 1, 여자는 2를 입력");
                    int genderInt = scanner.nextInt();
                    if (genderInt == 1) {
                        gender = 'm';
                        break;
                    } else if (genderInt == 2) {
                        gender = 'f';
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

            } else if (result == 2) {
                System.out.println("gender, country, grade index가 있습니다.");

            } else if (result == 3) {

            } else if (result == 4) {
                while (true) {
                    System.out.println("집계 함수를 실행할 column을 선택하세요.");
                    System.out.println("1. gender(성별)");
                    System.out.println("2. country(나라)");
                    System.out.println("3. grade(고객 등급)");
                    result = scanner.nextInt();

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
                        System.out.println(genderIndex.processCountQuery(countryScan));
                        break;

                    } else if (result == 3) {
                        System.out.println("grade key값을 입력하세요: 1~4 중에서 선택");
                        int gradeScan = scanner.nextInt();
                        if (gradeScan >= 1 && gradeScan <= 4) {
                            System.out.println(genderIndex.processCountQuery(String.valueOf(gradeScan)));
                        } else {
                            System.out.println("< Invalid input >");
                        }
                        break;

                    } else {
                        System.out.println("다시 입력해주세요.");
                    }
                }
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
//
//        genderIndex.add("m", 1);
//        genderIndex.add("m", 2);
//        genderIndex.add("f", 3);
//        genderIndex.add("f", 4);
//        genderIndex.add("f", 5);
//
//        countryIndex.add("Korea", 1);
//        countryIndex.add("Japan", 2);
//        countryIndex.add("Korea", 3);
//        countryIndex.add("Japan", 4);
//        countryIndex.add("Japan", 5);
//
//        Map<Bitmap, String> queryMap = new HashMap<>();
//        queryMap.put(genderIndex, "f");
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
