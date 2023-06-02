public class Main {
    public static void main(String[] args) {
        Bitmap genderIndex = Controller.readBitmapIndex(Controller.genderIndexFileName);
        Bitmap countryIndex = Controller.readBitmapIndex(Controller.countryIndexFileName);
        Bitmap gradeIndex = Controller.readBitmapIndex(Controller.gradeIndexFileName);
//        Bitmap genderIndex = new Bitmap("genderIndex");
//        Bitmap countryIndex = new Bitmap("countryIndex");
//        Bitmap gradeIndex = new Bitmap("gradeIndex");

        Controller controller = new Controller(genderIndex, countryIndex, gradeIndex);

        while (true) {
            System.out.println("\n< 고객 관리 Application >");
            System.out.println("1. record 삽입");
            System.out.println("2. DB에 있는 bitmap index 확인");
            System.out.println("3. multiple-key 질의");
            System.out.println("4. 집계함수 count(*) 처리");
            System.out.println("5. 튜플 자동 생성(20000개의 튜플 생성)");
            System.out.println("6. 종료");
            System.out.print("숫자를 입력해주세요: ");

            int result = controller.scanner.nextInt();

            if (result == 1) {
                controller.addOneTuple();

            } else if (result == 2) {
                System.out.println("gender, country, grade index가 있습니다.");

            } else if (result == 3) {
                controller.multipleKeyQuery();

            } else if (result == 4) {
                controller.countQuery();

            } else if (result == 5) {
                controller.addTuplesAuto();

            } else if (result == 6) {
                controller.exit();
                break;

            } else {
                System.out.println("\n<Invalid Input> 다시 입력해주세요");
            }
        }
    }
}
