package application;

class ThreadS1 implements Runnable{

    /*private Controller controller;*/

    public static int d1 = 0;
    public static int d2 = 0;

    int sync = Main.sync;
    int temp1 = 0;
    int temp2 = 0;

    
    /*public void run(Controller c) {
        controller = c;
    }*/

    public void run(){

        d1 = 0;
        d2 = 0;

        for(int i=0; i<=100; i++){ //Count 0 to 100
            temp1 = Main.temp1;
            temp2 = Main.temp2;
            if (Thread.currentThread().getName() == "th.1" && i == 100){
                if (sync == 1 && temp1 > temp2){ //Sync or not
                    try {
                        Thread.sleep(100); //Sleep to adjust timing
                        break;
                    } catch (InterruptedException e) {
                    }
                } else {
                    d1 = i / 2; //retrun int(50)
                    break;
                }
            } else if (Thread.currentThread().getName() == "th.2" && i ==100){
                if (sync == 1 && temp2 > temp1){
                    try {
                        Thread.sleep(100);
                        break;
                    } catch (InterruptedException e) {
                    }
                } else {
                    d2 = i / 2;
                    break;
                }
            }             
        }
        
        //debris
        /*if (sync == 1){
            for(int i=0; i<=100; i++){
                if (Thread.currentThread().getName() == "th.1" && i == 100){
                    d1 = controller.put1(i) / 2;
                    break;
                }                
            }
            for(int j=0; j<=100; j++){
                if(Thread.currentThread().getName() == "th.2" && j ==100){
                    d2 = controller.put2(j) / 2;
                    break;
                }
            }
        }*/
    }

}