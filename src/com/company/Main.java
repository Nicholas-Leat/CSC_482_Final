package com.company;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class Main {
    public static double LowestCost;
    public static int[] LowestPath = new int[15];
    public static int timesRun = 0;
    public static double[][] GenerateRandomCostMatrix(int numVert, int maxEdgeCost){
        double edgeCost[][] = new double[numVert][numVert];
        double temp;
        int num;
        Random rand = new Random();

        //Populates the edge cost array
        for(int i = 0; i < numVert; i++){
            for(int j = 0; j < numVert; j++){
                if(j != i){
                    temp = rand.nextDouble();
                    temp = temp % maxEdgeCost;
                    num = rand.nextInt(maxEdgeCost+1);
                    temp += num;
                    edgeCost[i][j] = temp;
                    edgeCost[j][i] = temp;
                }else{
                    edgeCost[i][j] = 0;
                }
            }
        }

        return edgeCost;
    }

    public static double[][] GenerateRandomEuclideanCostMatrix(int vert, int max){
        Random rand = new Random();
        double edgeCost[][] = new double[vert][vert];
        int x, y;
        //2d array to hold the location of each vertex
        int vertexLocation[][] = new int[vert][2];

        //place the vertex randomly on the graph0
        for(int i = 0; i < vert; i ++) {
            x = rand.nextInt(max);
            y = rand.nextInt(max);
            x++;
            y++;
            vertexLocation[i][0] = x;
            vertexLocation[i][1] = y;
        }
        //calculate the distance between
        for(int i = 0; i < vert; i++){
            for(int j = 0; j < vert; j++){
                if(i != j){
                    edgeCost[i][j] = Math.pow(vertexLocation[i][1] - vertexLocation[j][1],2) + Math.pow(vertexLocation[i][0] - vertexLocation[j][0],2);
                    if(edgeCost[i][j] < 0){
                        edgeCost[i][j] = edgeCost[i][j] * -1;
                    }
                    edgeCost[i][j] = Math.sqrt(edgeCost[i][j]);
                    edgeCost[j][i] = edgeCost[i][j];
                }
            }
        }

        return edgeCost;
    }

    public static double[][] GenerateRandomCircularGraphCostMatrix(int vert, int radius){
        Random rand = new Random();
        int x, y;
        double edgeCost[][] = new double[vert][vert];
        boolean placedVert[] = new boolean[vert];
        double vertexLocation[][] = new double[vert][2];
        int varplace[] = new int[vert];
        boolean allfill = false;
        double theta = (Math.PI*2)/vert;

        for(int i = 0; i < vert; i++){
            placedVert[i] = false;
        }
        int randVert = 0;
        //calculate the graph position equally around a circle
        for(int i = 0; i < vert; i++) {

            vertexLocation[randVert][0] = (radius * Math.cos(theta * i));//set x location
            vertexLocation[randVert][1] = (radius * Math.sin(theta * i));//set y location
            System.out.printf("%2d ", randVert);
            System.out.printf(": x: %10.1f  y:   %10.1f ", vertexLocation[randVert][0],vertexLocation[randVert][1]);
            System.out.println(" ");
            varplace[i] = randVert;
            placedVert[randVert] = true;
            if(randVert == vert){
                vert--;
            }
            for(int j= 0; j < vert; j++){
                if(placedVert[j] == false){
                    allfill = false;
                    break;
                }else{
                    allfill = true;
                }
            }
            if(allfill == true){
                break;
            }
            randVert = rand.nextInt(vert);
            if(placedVert[randVert] == true) {
                while (placedVert[randVert] == true) {
                    randVert = rand.nextInt(vert);
                }
            }
        }

        for(int i = 0; i < vert; i++){
            for(int j = 0; j < vert; j++){
                if(i != j){
                    edgeCost[i][j] = Math.pow(vertexLocation[j][1] - vertexLocation[i][1],2) + Math.pow(vertexLocation[j][0] - vertexLocation[i][0],2);
                    edgeCost[i][j] = Math.sqrt(edgeCost[i][j]);
                    edgeCost[j][i] = edgeCost[i][j];
                }
            }
        }

        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",varplace[i]);
        }
        System.out.println(" ");
        System.out.printf("Cost: %.2f\n", calccost(edgeCost,varplace,vert));
        return edgeCost;
    }
//************************************Greedy Algorithm*************************
     public static double greedy(double edgeCost[][], int vert){
        int next = 0;
        int current = 0;
        int set = 0;
        double cost = 0;
        int deadEnd = -1;
        double nextDist = -1;
        boolean canCon = true;
        boolean traveled[] = new boolean[vert];
        int travelList[] = new int[vert];
        for(int i = 0; i < vert; i++){
            traveled[i] = false;
            travelList[i] = 0;
        }
        boolean doneTravel = false;

        traveled[0] = true;
       for(int i = 1; i < vert; i++){
           if(nextDist == -1){
               nextDist = edgeCost[0][i];
               next = i;
           }else{
               if(edgeCost[0][i] < nextDist){
                   next = i;
                   nextDist = edgeCost[0][i];
               }
           }
       }
       current = next;
       nextDist = -1;
       travelList[1] = current;
       cost += edgeCost[0][current];
       traveled[current] = true;
       set = 2;
       while(doneTravel == false){
           for(int i = 0; i < vert; i++){
               if(nextDist == -1 && i != current && traveled[i] == false){
                   nextDist = edgeCost[current][i];
                   next = i;
               }else if(i != current && traveled[i] == false){
                   if(edgeCost[current][i] < nextDist){
                       next = i;
                       nextDist = edgeCost[current][i];
                   }
               }
           }
           travelList[set] = next;
           cost+= edgeCost[current][next];
           current = next;
           traveled[current] = true;
           nextDist = -1;
           set++;
           for(int i = 0; i < vert; i++){
               if(traveled[i] == true || set == vert){
                   doneTravel = true;
               }else{
                   doneTravel = false;
                   break;
               }
           }
       }
       cost += edgeCost[current][0];
       System.out.println("Greedy: ");
        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",travelList[i]);
        }
        System.out.println("   0 ");
        System.out.printf("Num verts: %d\n", vert);
        System.out.printf("Cost: %.2f\n", cost);/**/

         return cost;
     }
//*****************************************************************************
//*******************************Brute Force Algorithm*************************
    public static double bruteForce(double edgeCost[][], int vert){
        long val = 1;
        for(int i = 2; i < vert-1; i++){
            val = val * i;
        }

        int finalList[] = new int[vert];
        for(int i = 0; i < vert; i++){
            finalList[i] = i;
        }


        LowestCost = calccost(edgeCost,finalList,vert);
        //double cost = calccost(edgeCost,finalList,vert);

        finalList = permutation(edgeCost,finalList,1,vert-1,vert);
        //finalList = LowestPath;
        double cost = LowestCost;


        System.out.println("Brute Force: ");
        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",finalList[i]);
        }
        System.out.println("   0 ");
        System.out.printf("Num verts: %d\n", vert);
        System.out.printf("Cost: %.2f\n", cost);/**/
        return cost;
    }
    public static int[] permutation(double edgeCost[][], int[] nodes, int l, int r,int vert){
        double cost = calccost(edgeCost,nodes,vert);
        if(l == r){
            cost = calccost(edgeCost,nodes,vert);
            if(cost < LowestCost){
                LowestCost = cost;
                LowestPath = nodes;
            }
        }else{
            for(int i = l; i <=r; i++){
                nodes = swap(nodes,l,i);
                cost = calccost(edgeCost,nodes,vert);
                if(cost < LowestCost){
                    LowestCost = cost;
                    LowestPath = nodes;
                }
                nodes = permutation(edgeCost,nodes,l+1,r,vert);
                cost = calccost(edgeCost,nodes,vert);
                if(cost < LowestCost){
                    LowestCost = cost;
                    LowestPath = nodes;
                }
                //nodes = swap(nodes,l,i);


            }
        }

        return nodes;
    }
    public static int[] swap(int[] nodes,int l, int i){
        int temp = nodes[l];
        nodes[l] = nodes[i];
        nodes[i] = temp;


        return nodes;
    }
    public static double calccost(double[][] edgeCost,int[] nodes, int vert){
        double cost = 0;

        for(int i = 0; i < vert; i++){
            if( i == vert-1){
                cost+= edgeCost[nodes[i]][nodes[0]];
            }else{
                cost +=edgeCost[nodes[i]][nodes[i+1]];
            }
        }


        return cost;
    }
//*****************************************************************************
//**************************************Dynamic Algorithm**********************
    public static void dynamic(double edgeCost[][], int vert){
        int travelList[] = new int[vert];
        for(int i = 0; i < vert; i++){
            travelList[i] = i;
        }
        LowestCost = calccost(edgeCost,travelList,vert);

        travelList = permutation(edgeCost,travelList,1,vert-1,vert);
        double cost = LowestCost;

        System.out.println("Dynamic algorithm: ");
        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",travelList[i]);
        }
        System.out.println("   0 ");
        System.out.printf("Num verts: %d\n", vert);
        System.out.printf("Cost: %.2f\n", cost);
    }
//*****************************************************************************
    public static void testingExact(){
        int vert = 3;
        double time[] = new double[20];
        int count = 1;
        int N[] = new int[20];
        System.out.printf("N(Verts)          Time\n");

        while(count <=20){
            double edgeCost[][] = new double[vert][vert];
            edgeCost = GenerateRandomCostMatrix(vert,1000);

            double before = getCpuTime();
            bruteForce(edgeCost,vert);
            double after = getCpuTime();
            N[count-1] = vert;
            time[count-1] = after - before;
            System.out.printf("%8d      %4.2f\n",N[count-1], time[count-1]);
            count++;
            vert++;
        }


    }

    public static void timeGreedy(){
        int vert = 50;
        double time[] = new double[50];
        int count = 1;
        int N[] = new int[50];
        System.out.printf("N(Verts)          Time\n");
        boolean cont = true;

        while(cont == true){
            double edgeCost[][] = new double[vert][vert];
            edgeCost = GenerateRandomCircularGraphCostMatrix(vert,1000);

            double before = getCpuTime();
            greedy(edgeCost,vert);
            double after = getCpuTime();
            N[count-1] = vert;
            time[count-1] = after - before;
            System.out.printf("%8d      %4.2f\n",N[count-1], time[count-1]);
            count++;
            vert = vert * 2;
            if(time[count-2] > 1000000000){
                cont = false;
            }
        }
    }
    public static void calcSQR(){
        int vert = 4;
        int cont = 0;
        double costbf = 0;
        double costg = 0;
        double SQR[] = new double[9];
        while(cont < 9){
            double edgeCost[][] = new double[vert][vert];
            edgeCost = GenerateRandomCostMatrix(vert,1000);

            for(int i = 0; i < 5; i++) {
                costbf += bruteForce(edgeCost, vert);
                costg += greedy(edgeCost, vert);
            }
            costbf = costbf/5;
            costg = costg/5;
            SQR[cont] = costg/costbf;
            vert++;
            cont++;

        }
        for(int i = 0; i < 9; i++){
            System.out.printf("%6.2f",SQR[i]);
        }
        System.out.println(" ");

    }
    public static void main(String[] args) {
        Random rand = new Random();
        int vert = rand.nextInt(15);
        vert++;
        double edgeCost[][] = new double[vert][vert];

        //timeGreedy();
        //edgeCost = GenerateRandomCostMatrix(vert,1000);
        //edgeCost = GenerateRandomEuclideanCostMatrix(vert,1000);
        //edgeCost = GenerateRandomCircularGraphCostMatrix(vert,1000);
        //greedy(edgeCost,vert);
        //bruteForce(edgeCost,vert);
        calcSQR();;

        //testingExact();
        for(int i = 0; i < vert; i++){
            for(int j = 0; j < vert; j++){
                System.out.printf("%6.1f ", edgeCost[i][j]);
            }
            System.out.println(" ");
        }


        return;
    }
    public static long getCpuTime( ) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

        return ((ThreadMXBean) bean).isCurrentThreadCpuTimeSupported( ) ?

                bean.getCurrentThreadCpuTime( ) : 0L;

    }
}
