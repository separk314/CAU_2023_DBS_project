import java.io.*;
import java.util.*;

public class Controller {
    static int serialId = 0;

    static Scanner scanner = new Scanner(System.in);
    Bitmap genderIndex;
    Bitmap countryIndex;
    Bitmap gradeIndex;
    JDBC jdbc;

    static String genderIndexFileName = "genderIndex.bin";
    static String countryIndexFileName = "countryIndex.bin";
    static String gradeIndexFileName = "gradeIndex.bin";

    Controller(Bitmap genderIndex, Bitmap countryIndex, Bitmap gradeIndex) {
        this.genderIndex = genderIndex;
        this.countryIndex = countryIndex;
        this.gradeIndex = gradeIndex;

        jdbc = new JDBC();
        jdbc.connectMySQL();    // MySQL 연결
        jdbc.connectDatabase(); // CustomerDatabse 연결
        jdbc.createBPlusTreeIndex();    // B+tree index가 생성되어 있지 않다면 생성
    }



    void addOneTuple() {
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
        System.out.println(customer);

        // MySQL 데이터베이스에 레코드 삽입
        jdbc.insertRecord(customer);

        // bitmap index 업데이트
        genderIndex.add(gender, serialId);
        countryIndex.add(country, serialId);
        gradeIndex.add(String.valueOf(grade), serialId);

        serialId++;

        System.out.println("< 고객 생성 완료 >");

    }

    void addTuplesAuto() {
        // 20000개의 레코드를 생성합니다.
        for (int i=0; i<20000; i++) {
            String name = "customerName";

            String gender;
            if (i%2 == 0)   gender = "f";
            else gender = "m";

            String country;
            int grade = i%4 + 1;
            if (i < 5000) {
                country = "Korea";
            } else if (i < 10000) {
                country = "Japan";
            } else if (i < 15000) {
                country = "USA";
            } else {
                country = "UK";
            }

            Customer customer = new Customer(serialId, serialId, name, gender, country, grade);

            // MySQL 데이터베이스에 레코드 삽입
            jdbc.insertRecord(customer);

            // bitmap index 업데이트
            genderIndex.add(gender, serialId);
            countryIndex.add(country, serialId);
            gradeIndex.add(String.valueOf(grade), serialId);

            serialId++;
        }
    }

    void multipleKeyQuery() {
        Map<Bitmap, String> queries = new HashMap<>();
        String genderQuery = null;
        String countryQuery = null;
        String gradeQuery = null;

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
            countryQuery = "country=\""+countryResult+"\"";
        } else {
            System.out.println("country에 대한 질의를 생성하지 않음.");
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
            gradeQuery = "grade="+gradeResult+"";
        } else {
            System.out.println("gender에 대한 질의를 생성하지 않음.");
        }

        // Result
        long startTime = System.currentTimeMillis();
        Bitmap MultiKeyresult = genderIndex.processMultipleKeyQuery(queries);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (MultiKeyresult == null) {
            System.out.println("< 입력한 Query가 없습니다. >");
            return;
        }

        System.out.println("< Result >");
        String sqlString = "SELECT * FROM CUSTOMER WHERE ";

        if (genderQuery != null) {
            sqlString = sqlString + genderQuery;
            if (countryQuery != null || gradeQuery != null) sqlString = sqlString + " AND ";
        }
        if (countryQuery != null) {
            sqlString = sqlString + countryQuery;
            if (gradeQuery != null) sqlString = sqlString + " AND ";
        }
        if (gradeQuery != null) {
            sqlString = sqlString + gradeQuery;
        }

        System.out.println(sqlString);

        System.out.println("\n- Bitmap index를 사용한 시간: " + executionTime + " ms");
        jdbc.executeMultipleKeyQueryWithBPlusIndex(sqlString);
        System.out.println("\n"+ MultiKeyresult.getIndex().get("queryResult"));
    }

    void countQuery() {
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
                    long startTime = System.currentTimeMillis();
                    System.out.println("Total Count(Bitmap):"+ genderIndex.processCountQuery("m"));
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    System.out.println("Bitmap index를 사용한 시간: " + executionTime + " ms");

                    jdbc.executeGenderCountQueryWithBPlusIndex("m");

                } else if (genderScan == 2) {
                    long startTime = System.currentTimeMillis();
                    System.out.println("Total Count(Bitmap):"+ genderIndex.processCountQuery("f"));
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    System.out.println("Bitmap index를 사용한 시간: " + executionTime + " ms");

                    jdbc.executeGenderCountQueryWithBPlusIndex("f");

                } else {
                    System.out.println("< Invalid input >");
                }
                break;

            } else if (result == 2) {
                System.out.print("country key값을 입력하세요: ");
                String countryScan = scanner.next();
                int countQueryResult = countryIndex.processCountQuery(countryScan);
                System.out.println(countQueryResult);

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

    static void saveBitmapIndex(Bitmap bitmap, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bitmap);
            System.out.println(path+": index 저장");

        } catch (IOException error) {
            System.out.println(error);
            error.printStackTrace();
        }
    }

    static Bitmap readBitmapIndex(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Bitmap bitmap = (Bitmap) objectInputStream.readObject();
            System.out.println(path+": index 읽어오기");
            return bitmap;
        } catch (IOException | ClassNotFoundException error) {
            System.out.println(error);
            error.printStackTrace();
        }
        return null;
    }


    public void exit() {
        System.out.println("< 종료 >");
        Controller.saveBitmapIndex(genderIndex, "genderIndex.bin");
        Controller.saveBitmapIndex(countryIndex, "countryIndex.bin");
        Controller.saveBitmapIndex(gradeIndex, "gradeIndex.bin");

        jdbc.disconnectMySQL();
    }

}