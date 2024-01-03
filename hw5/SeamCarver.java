import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;
    private int W, H;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        W = picture.width();
        H = picture.height();
    }

    public Picture picture() { // current picture
        return picture;
    }

    public int width() { // width of current picture
        return W;
    }

    public int height() { // height of current picture
        return H;
    }

    private int plusX (int x) { return (x + 1) % W; }
    private int minusX(int x) { return (x - 1 + W) % W; }
    private int plusY(int y) { return (y + 1) % H; }
    private int minusY(int y) { return (y - 1 + H) % H; }
    private int getRGB(int x, int y, int rgb) { // rgb: 0 for red, 1 for green, 2 for blue
        Color color = picture.get(x, y);
        if (rgb == 0) return color.getRed();
        else if (rgb == 1) return color.getGreen();
        else return color.getBlue();
    }

    public double energy(int x, int y) { // energy of pixel at column x and row y
        if (x < 0 || x >= W || y < 0 || y >= H) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        double ret = 0;
        for (int i = 0; i < 3; ++i) {
            int tmp = getRGB(plusX(x), y, i) - getRGB(minusX(x), y, i);
            ret += tmp * tmp;
            tmp = getRGB(x, plusY(y), i) - getRGB(x, minusY(y), i);
            ret += tmp * tmp;
        }
        return ret;
    }

    public int[] findHorizontalSeam() {
        double[][] M = new double[H][W];
        int[][] father = new int[H][W];
    
        for (int y = 0; y < H; ++y) {
            M[y][0] = energy(0, y);
        }
    
        for (int x = 1; x < W; ++x) {
            for (int y = 0; y < H; ++y) {
                double YY = M[y][x - 1];
                double _Y = (y > 0) ? M[y - 1][x - 1] : Double.MAX_VALUE;
                double Y_ = (y < H - 1) ? M[y + 1][x - 1] : Double.MAX_VALUE;
    
                if (YY < _Y) {
                    if (YY < Y_) {
                        M[y][x] = energy(x, y) + YY;
                        father[y][x] = y;
                    } else {
                        M[y][x] = energy(x, y) + Y_;
                        father[y][x] = y + 1;
                    }
                } else {
                    if (_Y < Y_) {
                        M[y][x] = energy(x, y) + _Y;
                        father[y][x] = y - 1;
                    } else {
                        M[y][x] = energy(x, y) + Y_;
                        father[y][x] = y + 1;
                    }
                }
            }
        }
    
        int index = 0;
        for (int y = 1; y < H; ++y) {
            if (M[y][W - 1] < M[index][W - 1]) {
                index = y;
            }
        }
    
        int[] ret = new int[W];
        for (int x = W - 1; x >= 0; --x) {
            ret[x] = index;
            index = father[index][x];
        }
    
        return ret;
    }

    public int[] findVerticalSeam() { // sequence of indices for vertical seam
        double[][] M = new double[W][H]; // cost of minimum cost path ending at (i, j)
        int[][] father = new int[W][H];

        for (int x = 0; x < W; ++x) {
            M[x][0] = energy(x, 0);
        }

        for (int y = 1; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                double XX = M[x][y-1];
                double _X = (x>0) ? M[x-1][y-1] : Double.MAX_VALUE;
                double X_ = (x<W-1) ? M[x+1][y-1] : Double.MAX_VALUE;

                if (XX < _X) {
                    if (XX < X_) {
                        M[x][y] = energy(x, y) + XX;
                        father[x][y] = x;
                    } else {
                        M[x][y] = energy(x, y) + X_;
                        father[x][y] = x+1;
                    }
                } else {
                    if (_X < X_) {
                        M[x][y] = energy(x, y) + _X;
                        father[x][y] = x-1;
                    } else {
                        M[x][y] = energy(x, y) + X_;
                        father[x][y] = x+1;
                    }  
                }
            }
        }

        int index = 0;
        for (int x = 1; x < W; ++x) {
            if (M[x][H-1] < M[index][H-1]) {
                index = x;
            }
        }

        int[] ret = new int[H];
        for (int y = H - 1; y >= 0; --y) {
            ret[y] = index;
            index = father[index][y];
        }

        return ret;
    }

    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam) { // remove vertical seam from picture
        SeamRemover.removeVerticalSeam(picture, seam);
    }
}
