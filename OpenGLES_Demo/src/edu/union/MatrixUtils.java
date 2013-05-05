package edu.union;

public class MatrixUtils {
    

    /**
     * Returns the transpose of a 4x4 matrix
     * @param m The matrix to transpose
     * @param result The place to store the transposed matrix
     **/
    public static void transpose(float[][] m, float[][] result) {
	for (int i=0;i<4;i++)
	    for (int j=0;j<4;j++)
		result[j][i] = m[i][j];
    }
    
    public static void transpose(float[]m, float[] result) {
    	for (int i=0;i<4;i++)
    	    for (int j=0;j<4;j++)
    		result[j*4+i] = m[i*4+j];
    }

    /**
     * Converts this vector into a normalized (unit length) vector
     * <b>Modifies the input parameter</b>
     * @param vector The vector to normalize
     **/
    public static void normalize(float[] vector) {
    	scalarMultiply(vector, 1/magnitude(vector));
    }

    /**
     * Converts this vector into a normalized (unit length) vector
     * <b>Modifies the input parameter</b>
     * @param vector The vector to normalize
     **/
    public static void normalize(int[] vector) {
    	scalarMultiply(vector, 1/magnitude(vector));
    }


    /**
     * Copy a vector from <code>from</code> into <code>to</code>
     * @param from The source
     * @param to The destination
     **/
    public static void copy(float[] from, float[] to) {
	for (int i=0;i<from.length;i++) {
	    to[i] = from[i];
	}
    }

    /**
     * Multiply two matrices by each other and store the result. 
     * result = m1 x m2
     * @param m1 The first matrix
     * @param m2 The second matrix
     * @param reuslt Where to store the product of m1 x m2
     **/
    public static void multiply(float[][] m1, float[][] m2, float[][] result) {
	for (int i=0;i<4;i++) {
	    for (int j=0;j<4;j++) {
		result[i][j] = 
		    m1[i][0]*m2[0][j]+
		    m1[i][1]*m2[1][j]+
		    m1[i][2]*m2[2][j]+
		    m1[i][3]*m2[3][j];
	    }
	}
    }
    
    /**
     * Multiply a vector by a scalar.  <b>Modifies the input vector</b>
     * @param vector The vector 
     * @param scalar The scalar
     **/
    public static void scalarMultiply(float[] vector, float scalar) {
	for (int i=0;i<vector.length;i++)
	    vector[i] *= scalar;
    }
    
    /**
     * Multiply a vector by a scalar.  <b>Modifies the input vector</b>
     * @param vector The vector 
     * @param scalar The scalar
     **/
    public static void scalarMultiply(int[] vector, int scalar) {
	for (int i=0;i<vector.length;i++)
	    vector[i] = FixedPointUtils.multiply(vector[i],scalar);
    }


    /**
     * Create the identity matrix I
     * @param matrix The matrix to store the identity matrix in.
     **/
    public static void identity(float[][] matrix) {
	for (int i=0;i<4;i++)
	    for (int j=0;j<4;j++) 
		matrix[i][j] = (i==j)?1:0;
    }

    /**
     * Compute the dot product of two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @return v1 dot v2
     **/
    public static float dot(float[] v1, float[] v2) {
	float res = 0;
	for (int i=0;i<v1.length;i++)
	    res += v1[i]*v2[i];
	return res;
    }
    

    /**
     * Compute the cross product of two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @param result Where to store the cross product
     **/
    public static void cross(float[] p1, float[] p2, float[] result) {
	result[0] = p1[1]*p2[2]-p2[1]*p1[2];
	result[1] = p1[2]*p2[0]-p2[2]*p1[0];
	result[2] = p1[0]*p2[1]-p2[0]*p1[1];
    }
    
    /**
     * Compute the cross product of two vectors
     * @param v1 The first vector
     * @param v2 The second vector
     * @param result Where to store the cross product
     **/
    public static void cross(int[] p1, int[] p2, int[] result) {
	result[0] = FixedPointUtils.multiply(p1[1],p2[2])-FixedPointUtils.multiply(p2[1],p1[2]);
	result[1] = FixedPointUtils.multiply(p1[2],p2[0])-FixedPointUtils.multiply(p2[2],p1[0]);
	result[2] = FixedPointUtils.multiply(p1[0],p2[1])-FixedPointUtils.multiply(p2[0],p1[1]);
    }

    /**
     * Compute the magnitude (length) of a vector
     * @param vector The vector
     * @return The magnitude of the vector
     **/
    public static float magnitude(float[] vector) {
	return (float)Math.sqrt(vector[0]*vector[0]+
				vector[1]*vector[1]+
				vector[2]*vector[2]);
    }
    
    /**
     * Compute the magnitude (length) of a vector
     * @param vector The vector
     * @return The magnitude of the vector
     **/
    public static int magnitude(int[] vector) {
	return FixedPointUtils.sqrt(FixedPointUtils.multiply(vector[0],vector[0])+
								FixedPointUtils.multiply(vector[1],vector[1])+
								FixedPointUtils.multiply(vector[2],vector[2]));
    }

    public static void rotateZ(float[] v, float angle, float[] out) {
    	float[][] R = new float[4][4];
    	R[0][0] = (float)Math.cos(angle);
    	R[0][1] = (float)-Math.sin(angle);
    	R[1][0] = (float)Math.sin(angle);
    	R[1][1] = (float)Math.cos(angle);
    	R[2][2] = R[3][3] = 1;
    	
    	multiply(R, v, out);
    }
    
    public static void rotateY(float[] v, float angle, float[] out) {
    	float[][] R = new float[4][4];
    	R[0][0] = (float)Math.cos(angle);
    	R[0][2] = (float)-Math.sin(angle);
    	R[1][0] = (float)Math.sin(angle);
    	R[1][2] = (float)Math.cos(angle);
    	R[1][1] = R[3][3] = 1;
    	
    	multiply(R, v, out);
    }
    
    /**
     * Multiply a vector and a matrix.  result = matrix x vector
     * @param matrix The matrix.
     * @param vector The vector
     * @param result The result of the multiplication
     **/
    public static void multiply(float[][] matrix, float[] vector, float[] res)
    {
	for (int i=0;i<4;i++) {
	    res[i] = 
		matrix[i][0]*vector[0]+
		matrix[i][1]*vector[1]+
		matrix[i][2]*vector[2]+
		matrix[i][3]*vector[3];
	}
    }
    
    /**
     * Pretty print a matrix to stdout.
     * @param matrix The matrix
     **/
    public static void printMatrix(float[][] matrix) {
	for (int i=0;i<4;i++) {
	    for (int j=0;j<4;j++)
		System.out.print(matrix[i][j]+"\t");
	    System.out.println();
	}
    }
    
    /**
     * Homogenize a point (divide by its last element)
     * @param pt The point <b>Modified</b>
     **/
    public static void homogenize(float[] pt)
    {
    	scalarMultiply(pt, 1/pt[3]);
    }

    /**
     * Pretty print a vector
     * @param vec The vector to print
     **/
    public static void printVector(float[] vec) {
	for (int i=0;i<vec.length;i++)
	    System.out.println(vec[i]);
    }

    /**
     * Subtracts two vectors (a-b).
     * @param a The first vector
     * @param b The second vector
     * @param result Storage for the result, if null, store in a.
     **/
    public static void minus(float[] a, float[] b, float[] result) {
	float[] res = (result == null)?a:result;
	for (int i=0;i<Math.min(a.length,b.length);i++)
	    res[i] = a[i]-b[i];
    }
    
    /**
     * Subtracts two vectors (a-b).
     * @param a The first vector
     * @param b The second vector
     * @param result Storage for the result, if null, store in a.
     **/
    public static void minus(int[] a, int[] b, int[] result) {
	int[] res = (result == null)?a:result;
	for (int i=0;i<Math.min(a.length,b.length);i++)
	    res[i] = a[i]-b[i];
    }

    /**
     * Adds two vectors (a+b).
     * @param a The first vector
     * @param b The second vector
     * @param result Storage for the result, if null, store in a.
     **/
    public static void plus(float[] a, float[] b, float[] result) {
	float[] res = (result == null)?a:result;
	for (int i=0;i<a.length;i++)
	    res[i] = a[i]+b[i];
    }
    
    /**
     * Adds two vectors (a+b).
     * @param a The first vector
     * @param b The second vector
     * @param result Storage for the result, if null, store in a.
     **/
    public static void plus(int[] a, int[] b, int[] result) {
	int[] res = (result == null)?a:result;
	for (int i=0;i<a.length;i++)
	    res[i] = a[i]+b[i];
    }
}

