import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> studentArrayDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> arrayDequeSolution = new ArrayDequeSolution<>();
        String log = "";

        while (true) {
            int n = StdRandom.uniform(100);
            int choice = StdRandom.uniform(4);
            if (arrayDequeSolution.size() == 0) {
                if (choice < 2) {
                    log += "addFirst(" + n + ")\n";
                    studentArrayDeque.addFirst(n);
                    arrayDequeSolution.addFirst(n);
                } else {
                    log += "addLast(" + n + ")\n";
                    studentArrayDeque.addLast(n);
                    arrayDequeSolution.addLast(n);
                }
            } else {
                Integer studentNum = 0;
                Integer solutionNum = 0;
                switch (choice) {
                    case 0:
                        log += "addFirst(" + n + ")\n";
                        studentArrayDeque.addFirst(n);
                        arrayDequeSolution.addFirst(n);
                        break;
                    case 1:
                        log += "addLast(" + n + ")\n";
                        studentArrayDeque.addLast(n);
                        arrayDequeSolution.addLast(n);
                        break;
                    case 2:
                        log += "removeFirst()\n";
                        studentNum = studentArrayDeque.removeFirst();
                        solutionNum = arrayDequeSolution.removeFirst();
                        break;
                    case 3:
                        log += "removeLast()\n";
                        studentNum = studentArrayDeque.removeLast();
                        solutionNum = arrayDequeSolution.removeLast();
                        break;
                    default:
                        break;
                }
                assertEquals(log, studentNum, solutionNum);
            }
        }
    }
}