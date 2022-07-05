package application;

public class Controller {
    private boolean available1 = false;
    private boolean available2 = false;
    int ans = 100;

    public synchronized int put1(int i) {
        while (available1) {
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){}
        }
        available1 = true;
        notifyAll();
        return ans;
    }

    public synchronized int put2(int i) {
        while (available2) {
            try{
              Thread.sleep(100);
            } catch (InterruptedException e){}
        }
        available2 = true;
        notifyAll();
        return ans;
    }

}
    



