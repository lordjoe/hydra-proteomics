package org.systemsbiology.asa;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.asa.Asa
 * User: Steve
 * Routines to calculate the Accessible Surface Area of a set of atoms.
 * The algorithm is adapted from the Rose lab's chasa.py, which uses
 * the dot density technique found in:
 * <p/>
 * Shrake, A., and J. A. Rupley. "Environment and Exposure to Solvent
 * of Protein Atoms. Lysozyme and Insulin." JMB (1973) 79:351-371.
 * Adapted from the Python Code of
 * http://boscoh.com/protein/asapy
 * <p/>
 * Date: 7/11/12
 */
public class Asa {
    public static final Asa[] EMPTY_ARRAY = {};
    public static final int DEFAULT_SPHERE_POINTS = 960;
    public static final double WATER_RADIUS = 1.4;

    /*

"""
Routines to calculate the Accessible Surface Area of a set of atoms.
The algorithm is adapted from the Rose lab's chasa.py, which uses
the dot density technique found in:

Shrake, A., and J. A. Rupley. "Environment and Exposure to Solvent
of Protein Atoms. Lysozyme and Insulin." JMB (1973) 79:351-371.
"""


import math
from vector3d import pos_distance, Vector3d, pos_distance_sq
def generate_sphere_points(n):
    """
    Returns list of 3d coordinates of points on a sphere using the
    Golden Section Spiral algorithm.
    """
    points = []
    inc = Math.pi * (3 - Math.sqrt(5))
    offset = 2 / float(n)
    for k in range(int(n)):
        y = k * offset - 1 + (offset / 2)
        r = Math.sqrt(1 - y*y)
        phi = k * inc
        points.append([Math.cos(phi)*r, y, Math.sin(phi)*r])
    return points
 
*/

    /**
     * Returns list of 3d coordinates of points on a sphere using the
     * Golden Section Spiral algorithm.
     *
     * @param n number points
     * @return !null array of points
     */
    public static Point3d[] generate_sphere_points(int n) {
        Point3d[] ret = new Point3d[n];
        double inc = Math.PI * (3 - Math.sqrt(5));
        double offset = 2 / (double) n;
        for (int i = 0; i < ret.length; i++) {
            double y = i * offset - 1 + (offset / 2);
            double r = Math.sqrt(1 - y * y);
            double phi = i * inc;
            ret[i] = new Point3d(Math.cos(phi) * r, y, Math.sin(phi) * r);

        }
        return ret;
    }
/*

def find_neighbor_indices(atoms, probe, k):
    """
    Returns list of indices of atoms within probe distance to atom k.
    """
    neighbor_indices = []
    atom_k = atoms[k]
    radius = atom_k.radius + probe + probe
    indices = range(k)
    indices.extend(range(k+1, len(atoms)))
    for i in indices:
        atom_i = atoms[i]
        dist = pos_distance(atom_k.pos, atom_i.pos)
        if dist < radius + atom_i.radius:
            neighbor_indices.append(i)
    return neighbor_indices
*/

    /**
     * Returns list of indices of atoms within probe distance to atom k.
     *
     * @param atoms
     * @param probe
     * @param k
     * @return
     */
    public static int[] find_neighbor_indices(AsaAtom[] atoms, double probe, int k) {
        List<Integer> holder = new ArrayList<Integer>();
        AsaAtom atom_k = atoms[k];
        Point3d pos = atom_k.getPos();
        double radius = atom_k.getRadius() + probe + probe;
        for (int i = 0; i < atoms.length; i++) {
            if (i == k)
                continue;
            AsaAtom atom = atoms[i];
            double dist = pos.distance(atom.getPos());
            if (dist < radius + atom.getRadius())
                holder.add(i);
        }

        Integer[] held = new Integer[holder.size()];
        holder.toArray(held);
        int[] ret = new int[holder.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = held[i];
        }
        return ret;
    }

    /*

    def calculate_asa(atoms, probe, n_sphere_point=960):
        """
        Returns list of accessible surface areas of the atoms, using the probe
        and atom radius to define the surface.
        """
        sphere_points = generate_sphere_points(n_sphere_point)

        const = 4.0 * Math.pi / len(sphere_points)
        test_point = Vector3d()
        areas = []
        for i, atom_i in enumerate(atoms):

            neighbor_indices = find_neighbor_indices(atoms, probe, i)
            n_neighbor = len(neighbor_indices)
            j_closest_neighbor = 0
            radius = probe + atom_i.radius

            n_accessible_point = 0
            for point in sphere_points:
                is_accessible = True

                test_point.x = point[0]*radius + atom_i.pos.x
                test_point.y = point[1]*radius + atom_i.pos.y
                test_point.z = point[2]*radius + atom_i.pos.z

                cycled_indices = range(j_closest_neighbor, n_neighbor)
                cycled_indices.extend(range(j_closest_neighbor))

                for j in cycled_indices:
                    atom_j = atoms[neighbor_indices[j]]
                    r = atom_j.radius + probe
                    diff_sq = pos_distance_sq(atom_j.pos, test_point)
                    if diff_sq < r*r:
                        j_closest_neighbor = j
                        is_accessible = False
                        break
                if is_accessible:
                    n_accessible_point += 1

            area = const*n_accessible_point*radius*radius
            areas.append(area)
        return areas
    */

    /**
      * Returns list of accessible surface areas of the atoms, using the probe
      * and atom radius to define the surface.
      *
      * @param atoms
      * @param probe solvent radius
      * @return
      */
     public static AsaAtom[] calculate_asa(AsaAtom[] atoms, double probe) {
         return calculate_asa(atoms, probe, DEFAULT_SPHERE_POINTS);
     }
    /**
      * Returns list of accessible surface areas of the atoms, using the probe
      * and atom radius to define the surface.
      *
      * @param atoms
        * @return
      */
     public static AsaAtom[] calculate_asa(AsaAtom[] atoms ) {
         return calculate_asa(atoms, WATER_RADIUS);
     }
    /**
      * Returns list of accessible surface areas of the atoms, using the probe
      * and atom radius to define the surface.
      *
      * @param atoms
        * @return
      */
     public static AsaAtom[] calculate_asa(AsaMolecule mol ) {
         return calculate_asa(mol.getAtoms());
     }

    /**
     * Returns list of accessible surface areas of the atoms, using the probe
     * and atom radius to define the surface.
     *
     * @param atoms
     * @param probe          solvent radius
     * @param n_sphere_point points on a sphere
     * @return
     */
    public static AsaAtom[] calculate_asa(AsaAtom[] atoms, double probe, int n_sphere_point) {
        Point3d[] sphere_points = generate_sphere_points(n_sphere_point);

        double constant = 4.0 * Math.PI / sphere_points.length;
        Point3d test_point = new Point3d(0, 0, 0);
        List<AsaAtom> areas = new ArrayList<AsaAtom>();
        System.err.println("Number atoms = " + atoms.length);
        for (int i = 0; i < atoms.length; i++) {
            AsaAtom atom_i = atoms[i];
            if(i % 1000 == 0)
                System.err.println("Working on = " + i);

            Point3d pos = atom_i.getPos();
            int[] neighbor_indices = find_neighbor_indices(atoms, probe, i);
            int n_neighbor = neighbor_indices.length;
            int j_closest_neighbor = 0;
            double radius = probe + atom_i.getRadius();
            int n_accessible_point = 0;
            for (int k = 0; k < sphere_points.length; k++) {
                Point3d point = sphere_points[k];
                boolean is_accessible = true;
                test_point.x = point.x * radius + pos.x;
                test_point.y = point.y * radius + pos.y;
                test_point.z = point.z * radius + pos.z;
                for (int j = 0; j < neighbor_indices.length; j++) {
                    int neighbor_indice = neighbor_indices[j];
                    AsaAtom atom_j = atoms[neighbor_indice];
                    double r = atom_j.getRadius() + probe;
                    double dist = test_point.distance(atom_j.getPos());
                    if (dist < r) {
                        j_closest_neighbor = j;
                        is_accessible = false;
                        break;
                    }
                }
                if (is_accessible)
                    n_accessible_point++;
            }
            if (n_accessible_point > 0) {
                double area = constant * n_accessible_point * radius * radius;
                atom_i.setAccessible(true);
                atom_i.setAccessibleArea(area);
                areas.add(atom_i);
            }
            else {
                atom_i.setAccessible(false);

            }

        }
        AsaAtom[] ret = new AsaAtom[areas.size()];
        areas.toArray(ret);
        return ret;
        
    }
    /*
    areas = []
    for i, atom_i in enumerate(atoms):

        neighbor_indices = find_neighbor_indices(atoms, probe, i)
        n_neighbor = len(neighbor_indices)
        j_closest_neighbor = 0
        radius = probe + atom_i.radius

        n_accessible_point = 0
        for point in sphere_points:
            is_accessible = True

            test_point.x = point[0]*radius + atom_i.pos.x
            test_point.y = point[1]*radius + atom_i.pos.y
            test_point.z = point[2]*radius + atom_i.pos.z

            cycled_indices = range(j_closest_neighbor, n_neighbor)
            cycled_indices.extend(range(j_closest_neighbor))

            for j in cycled_indices:
                atom_j = atoms[neighbor_indices[j]]
                r = atom_j.radius + probe
                diff_sq = pos_distance_sq(atom_j.pos, test_point)
                if diff_sq < r*r:
                    j_closest_neighbor = j
                    is_accessible = False
                    break
            if is_accessible:
                n_accessible_point += 1

        area = const*n_accessible_point*radius*radius
        areas.append(area)
    return areas
    */

    private static void readPDBMolecule(File pdb) {
         AsaMolecule mol  = AsaMolecule.fromPDB(pdb);
         AsaAtom[] atoms = mol.getAtoms();

         int n_sphere = DEFAULT_SPHERE_POINTS;
         AsaAtom[] asaAtoms = calculate_asa(atoms, WATER_RADIUS, n_sphere);

         AsaSubunit[] accessibleSubunits = mol.getAccessibleSubunits();
         AsaSubunit[] inaccessibleSubunits = mol.getInaccessibleSubunits();
         for (int i = 0; i < inaccessibleSubunits.length; i++) {
             AsaSubunit inaccessibleSubunit = inaccessibleSubunits[i];

         }
     }

    /**
     * Usage: asa.py -s n_sphere in_pdb [out_pdb]
     * <p/>
     * - out_pdb    PDB file in which the atomic ASA values are written
     * to the b-factor column.
     * <p/>
     * -s n_sphere  number of points used in generating the spherical
     * dot-density for the calculation (default=960). The
     * more points, the more accurate (but slower) the
     * calculation.
     *
     * @param args
     */
    public static void main(String[] args) {
         File pdb = new File("1I72.pdb");
         readPDBMolecule(pdb);

        File[] files = new File(System.getProperty("user.dir")).listFiles();
        for (int i = 1300; i < files.length; i++) {
            pdb = files[i];
            System.out.println(pdb + " " + i);
            if(pdb.getName().endsWith(".pdb"))
                readPDBMolecule(pdb);

        }
     //   File pdb = new File(args[0]);
         //   print "%.1f angstrom squared." % sum(asas)
//
//        if len(args) > 1:
//          for asa, atom in zip(asas, atoms):
//            atom.bfactor = asa
//          mol.write_pdb(args[1])

    }

     /*
def main():
 import sys
 import getopt
 import molecule


 usage = \
 """

 Copyright (c) 2007 Bosco Ho

 Calculates the total Accessible Surface Area (ASA) of atoms in a
 PDB file.

 Usage: asa.py -s n_sphere in_pdb [out_pdb]

 - out_pdb    PDB file in which the atomic ASA values are written
              to the b-factor column.

 -s n_sphere  number of points used in generating the spherical
              dot-density for the calculation (default=960). The
              more points, the more accurate (but slower) the
              calculation.

 """

 opts, args = getopt.getopt(sys.argv[1:], "n:")
 if len(args) < 1:
   print usage
   return

 mol = molecule.Molecule(args[0])
 atoms = mol.atoms()
 molecule.add_radii(atoms)

 n_sphere = 960
 for o, a in opts:
   if '-n' in o:
     n_sphere = int(a)
     print "Points on sphere: ", n_sphere
 asas = calculate_asa(atoms, 1.4, n_sphere)
 print "%.1f angstrom squared." % sum(asas)

 if len(args) > 1:
   for asa, atom in zip(asas, atoms):
     atom.bfactor = asa
   mol.write_pdb(args[1])


if __name__ == "__main__":
 main()



    */

}
