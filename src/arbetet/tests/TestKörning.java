package arbetet.tests;

import java.awt.*;
import java.util.LinkedList;

public class TestKörning {
    public static void main(String[] args) {
        int height = 16;
        int width = 16;
        LinkedList<Point> pointlista = new LinkedList<>();
        pointlista.add(new Point(0,0));
        Point nu = pointlista.get(0);
        Point förra = null;
        while (nu.y<height-1) {
            int x = nu.x;
            int y = nu.y;
            int vilketHåll = (int) (Math.random()*30);
            Point p;
            if (vilketHåll<9){
                p = new Point(nu.x, nu.y+1);
            }
            else if (vilketHåll<14 & vilketHåll>=9 ){
                if (y==0) continue;
                p = new Point(nu.x, nu.y-1);
            }
            else if (vilketHåll<24 & vilketHåll>=14){
                if (x==width-1) continue;
                p = new Point(nu.x+1, nu.y);
            }
            else{
                if (x==0) continue;
                p = new Point(nu.x-1, nu.y);
            }
            if (p.equals(förra)) continue;
            pointlista.add(p);
            förra = nu;
            nu = p;
        }
        System.out.println(pointlista);
        System.out.println(pointlista.size());
    }
}
