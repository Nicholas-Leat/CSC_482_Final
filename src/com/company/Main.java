package com.company;
import java.util.Random;

public class Main {
    public static double LowestCost;
    public static int[] LowestPath;
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
        double theta = 360/vert;

        for(int i = 0; i < vert; i++){
            placedVert[i] = false;
        }
        int randVert = 0;
        //calculate the graph position equally around a circle
        for(int i = 0; i < vert; i++) {

            vertexLocation[randVert][0] = (radius * Math.cos(theta * randVert));//set x location
            vertexLocation[randVert][1] = (radius * Math.sin(theta * randVert));//set y location
            placedVert[randVert] = true;
            if(randVert == vert){
                vert--;
            }
            randVert = rand.nextInt(vert);
            if(placedVert[randVert] == true) {
                while (placedVert[randVert] == false) {
                    randVert = rand.nextInt(vert);
                }
            }
        }

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
//************************************Greedy Algorithm*************************
     public static void greedy(double edgeCost[][], int vert){
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
        System.out.printf("Cost: %.2f\n", cost);

     }
//*****************************************************************************
//*******************************Brute Force Algorithm*************************
    public static void bruteForce(double edgeCost[][], int vert){
        long val = 1;
        for(int i = 2; i < vert-1; i++){
            val = val * i;
        }

        int finalList[] = new int[vert];
        for(int i = 0; i < vert; i++){
            finalList[i] = i;
        }
        //double cost = calccost(edgeCost,finalList,vert);

        finalList = permutation(edgeCost,finalList,1,vert-1,vert);
        double cost = calccost(edgeCost,finalList,vert);







        System.out.println("Brute Force: ");
        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",finalList[i]);
        }
        System.out.println("   0 ");
        System.out.printf("Num verts: %d\n", vert);
        System.out.printf("Cost: %.2f\n", cost);
    }
    public static int[] permutation(double edgeCost[][], int[] nodes, int l, int r,int vert){

        if(l == r){
            double cost = calccost(edgeCost,nodes,vert);
            if(cost < LowestCost){
                LowestCost = cost;
                LowestPath = nodes;
            }
        }else{
            for(int i = l; i <=r; i++){
                nodes = swap(nodes,l,i);
                nodes = permutation(edgeCost,nodes,l+1,r,vert);
                nodes = swap(nodes,l,i);
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
        int cost = 0;



        System.out.println("Brute Force: ");
        for(int i = 0; i < vert; i++){
            System.out.printf("%3d ",travelList[i]);
        }
        System.out.println("   0 ");
        System.out.printf("Num verts: %d\n", vert);
        System.out.printf("Cost: %.2f\n", cost);
    }
//*****************************************************************************
    public static void main(String[] args) {
        Random rand = new Random();
        int vert = rand.nextInt(15);
        vert++;
        double edgeCost[][] = new double[vert][vert];

        edgeCost = GenerateRandomCostMatrix(vert,1000);
        //edgeCost = GenerateRandomEuclideanCostMatrix(vert,1000);
        //edgeCost = GenerateRandomCircularGraphCostMatrix(vert,1000);
        greedy(edgeCost,vert);
        bruteForce(edgeCost,vert);

        for(int i = 0; i < vert; i++){
            for(int j = 0; j < vert; j++){
                System.out.printf("%6.1f ", edgeCost[i][j]);
            }
            System.out.println(" ");
        }


        return;
    }
}
