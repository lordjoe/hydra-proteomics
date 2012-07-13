package org.systemsbiology.asa;

import org.systemsbiology.jmol.*;

import javax.vecmath.*;

/**
 * org.systemsbiology.asa.AsaAtom
 * User: Steve
 * Date: 7/11/12
 */
public class AsaAtom implements Comparable<AsaAtom> {
    public static final AsaAtom[] EMPTY_ARRAY = {};

    private boolean m_HetAtom;
    private Point3d m_Pos;
    private Point3d m_Vel;
    private double m_Mass;
    private int m_Num;
    private double m_BFActor;
    private double m_Occupancy;
    private Element m_Element;
    private String m_Type;
    private ChainEnum m_ChainId;
    private String m_ResType;
    private int m_ResNum;
    private String m_Res_Inset;
    private boolean m_Accessible;
    private double m_AccessibleArea;

    public AsaAtom(String line) {
        setHetAtom(line.startsWith("HETATM"));
        String s;
        s = line.substring(6, 12).trim();
        m_Num = Integer.parseInt(s);
        s = line.substring(12, 17).trim();
        m_Type = s;
        m_Element = Element.fromString(s);

//        if element[:2] in two_char_elements:
//          atom.element = element[:2]
//        else:
//          atom.element = element[0]
//        atom.res_type = line[17:20]
        s = line.substring(17, 21).trim();
        m_ResType = s;
//        atom.chain_id = line[21]
        s = line.substring(21, 22);
        m_ChainId = ChainEnum.fromString(s);
//        atom.res_num = int(line[22:26])
        s = line.substring(22, 26).trim();
        m_ResNum = Integer.parseInt(s);

//        atom.res_insert = line[26]
        s = line.substring(26, 27).trim();
        m_Res_Inset = s;

//        if atom.res_insert == " ":
//          atom.res_insert = ""
        s = line.substring(30, 38).trim();
        double x = Double.parseDouble(s);
        s = line.substring(38, 46).trim();
        double y = Double.parseDouble(s);
        s = line.substring(46, 54).trim();
        double z = Double.parseDouble(s);
//        x = float(line[30:38])
//        y = float(line[38:46])
//        z = float(line[46:54])
//        atom.pos.set(x, y, z)
        setPos(new Point3d(x, y, z));
    }

    /*
class Atom:
 def __init__(self):
   self.is_hetatm = False
   self.pos = Point3d.Point3d()
   self.vel = Point3d.Point3d()
   self.mass = 0.0
   self.type = ""
   self.element = ""
   self.chain_id = " "
   self.res_type = ""
   self.res_num = ""
   self.res_insert = ""
   self.bfactor = 0.0
   self.occupancy = 0.0
   self.num = 0


def AtomFromPdbLine(line):
 """Returns an Atom object from an atom line in a pdb file."""
 atom = Atom()
 if line.startswith('HETATM'):
   atom.is_hetatm = True
 else:
   atom.is_hetatm = False
 atom.num = int(line[6:11])
 atom.type = line[12:16].strip(" ")
 element = ''
 for c in line[12:15]:
   if not c.isdigit() and c != " ":
     element += c
 if element[:2] in two_char_elements:
   atom.element = element[:2]
 else:
   atom.element = element[0]
 atom.res_type = line[17:20]
 atom.chain_id = line[21]
 atom.res_num = int(line[22:26])
 atom.res_insert = line[26]
 if atom.res_insert == " ":
   atom.res_insert = ""
 x = float(line[30:38])
 y = float(line[38:46])
 z = float(line[46:54])
 atom.pos.set(x, y, z)
 try:
   atom.occupancy = float(line[54:60])
 except:
   atom.occupancy = 100.0
 try:
   atom.bfactor = float(line[60:66])
 except:
   atom.bfactor = 0.0
 return atom
*/

    public boolean isHetAtom() {
        return m_HetAtom;
    }

    public void setHetAtom(final boolean hetAtom) {
        m_HetAtom = hetAtom;
    }

    public Point3d getPos() {
        return m_Pos;
    }

    public void setPos(final Point3d pos) {
        m_Pos = pos;
    }

    public Point3d getVel() {
        return m_Vel;
    }

    public void setVel(final Point3d vel) {
        m_Vel = vel;
    }

    public double getMass() {
        return m_Mass;
    }

    public void setMass(final double mass) {
        m_Mass = mass;
    }

    public int getNum() {
        return m_Num;
    }

    public void setNum(final int num) {
        m_Num = num;
    }

    public double getBFActor() {
        return m_BFActor;
    }

    public void setBFActor(final double BFActor) {
        m_BFActor = BFActor;
    }

    public double getOccupancy() {
        return m_Occupancy;
    }

    public void setOccupancy(final double occupancy) {
        m_Occupancy = occupancy;
    }

    public String getType() {
        return m_Type;
    }

    public void setType(final String type) {
        m_Type = type;
    }

    public Element getElement() {
        return m_Element;
    }

    public double getRadius() {
        return getElement().getRadius();
    }

    public void setElement(final Element element) {
        m_Element = element;
    }

    public ChainEnum getChainId() {
        return m_ChainId;
    }

    public void setChainId(final ChainEnum chainId) {
        m_ChainId = chainId;
    }

    public String getResType() {
        return m_ResType;
    }

    public void setResType(final String resType) {
        m_ResType = resType;
    }

    public int getResNum() {
        return m_ResNum;
    }

    public void setResNum(final int resNum) {
        m_ResNum = resNum;
    }

    public String getRes_Inset() {
        return m_Res_Inset;
    }

    public void setRes_Inset(final String res_Inset) {
        m_Res_Inset = res_Inset;
    }

    public boolean isAccessible() {
        return m_Accessible;
    }

    public void setAccessible(boolean accessible) {
        m_Accessible = accessible;
    }

    public double getAccessibleArea() {
        return m_AccessibleArea;
    }

    public void setAccessibleArea(double accessibleArea) {
        m_AccessibleArea = accessibleArea;
    }

    /*
   def cmp_atom(a1, a2):
    if a1.num < a2.num:
      return -1
    else:
      return 0
    */
    @Override
    public int compareTo(final AsaAtom o) {
        int num = getNum();
        int onum = o.getNum();
        if (num == onum)
            return 0;
        return num < onum ? -1 : 1;
    }

    /*
def pad_atom_type(in_atom_type):
 atom_type = in_atom_type
 if len(atom_type) == 1:
   atom_type = " %s  " % atom_type
 elif len(atom_type) == 2:
   atom_type = " %s " % atom_type
 elif len(atom_type) == 3:
   if atom_type[0].isdigit():
     atom_type = "%s " % atom_type
   else:
     atom_type = " %s" % atom_type
 return atom_type


module_dir = os.path.dirname(__file__)
radii_fname = os.path.join(module_dir, "radii.txt")
f = open(radii_fname, 'r')
radii = eval(f.read())
f.close()
two_char_elements = [el for el, r in radii.items() if len(el) == 2]


def add_radii(atoms):
 for atom in atoms:
   if atom.element in radii:
     atom.radius = radii[atom.element]
   else:
     atom.radius = radii['.']

 */


    /*
def get_center(atoms):
 center = Point3d.Point3d(0, 0, 0)
 for atom in atoms:
   center += atom.pos
 center.scale(1.0/float(len(atoms)))
 return center
  */
    public static Point3d center(AsaAtom[] atoms) {
        double x = 0;
        double y = 0;
        double z = 0;
        for (int i = 0; i < atoms.length; i++) {
            Point3d atom = atoms[i].getPos();
            x += atom.x;
            y += atom.y;
            z += atom.z;
        }
        return new Point3d(x, y, z);
    }

    /*
def get_width(atoms, center):
 max_diff = 0
 for atom in atoms:
   diff = Point3d.pos_distance(atom.pos, center)
   if diff > max_diff:
     max_diff = diff
 return 2*max_diff

    */
    public static double width(AsaAtom[] atoms) {
        Point3d cntr = center(atoms);
        return width(atoms, cntr);
    }

    public static double width(final AsaAtom[] atoms, final Point3d cntr) {
        double dist = 0;
        double ret = 0;
        for (int i = 0; i < atoms.length; i++) {
            dist = Math.max(dist, atoms[i].getPos().distance(cntr));
        }
        return dist;
    }

    public void transform(Matrix3d mx) {
        mx.transform(getPos());
    }

    /*
 def pdb_str(self):
   if self.is_hetatm:
     field = "HETATM"
   else:
     field = "ATOM  "
   return "%6s%5s %4s %3s %1s%4d%1s   %8.3f%8.3f%8.3f%6.2f%6.2f" \
           % (field, self.num,
              pad_atom_type(self.type),
              self.res_type, self.chain_id,
              self.res_num, self.res_insert,
              self.pos.x, self.pos.y, self.pos.z,
              self.occupancy, self.bfactor)

 def __str__(self):
   return "%s%s-%s (% .1f % .1f % .1f)" \
           %  (self.res_type, self.res_num,
               self.type, self.pos.x,
               self.pos.y, self.pos.z)


     */
    public String asPDB() {
        StringBuilder sb = new StringBuilder();
        if (isHetAtom())
            sb.append("ATOM  ");
        else
            sb.append("HETATM");

        Vector3d pos = getPos();
        String line = String.format("%6s%5s %4s %3s %1s%4d%1s   %8.3f%8.3f%8.3f%6.2f%6.2f",
                m_Num,
                pad_atom_type(m_Type),
                m_ResType, m_ChainId,
                m_ResNum, m_Res_Inset,
                pos.x, pos.y, pos.z,
                m_Occupancy, m_BFActor
        );
        sb.append(line);
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String suString = AsaSubunit.buildSubUnitString(getChainId().toString(), getResType(), getResNum());
        sb.append(suString + "-" + getElement() + getNum());
        return sb.toString();
    }

    protected String pad_atom_type(String el) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }


}
