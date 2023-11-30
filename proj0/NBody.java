public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int N = in.readInt();
        double R = in.readDouble();
        return R;
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int N = in.readInt();
        double R = in.readDouble();
        Planet[] planets = new Planet[N];
        for (int i = 0; i < N; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            planets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return planets;
    }   

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String fileName = args[2];
        double R = readRadius(fileName);
        Planet[] planets = readPlanets(fileName);

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-R, R);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        
        for (Planet p : planets) {
            p.draw();
        }      
        StdDraw.show();      

        double t = 0;
        double[] xForces = new double[planets.length];
        double[] yForces = new double[planets.length];

        while (t < T) {
            for (int i = 0; i < planets.length; i++) {
                Planet p = planets[i];
                xForces[i] = p.calcNetForceExertedByX(planets);
                yForces[i] = p.calcNetForceExertedByY(planets); 
            }
            for (int i = 0; i < planets.length; i++) {
                Planet p = planets[i];
                p.update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : planets) {
                p.draw();
            }      
            StdDraw.show();      
            StdDraw.pause(10);
            t += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", R);
        for (Planet p : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", 
                p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);   
        }
    }
}
