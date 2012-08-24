package org.systemsbiology.asa;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;

import javax.vecmath.*;
import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.asa.AsaMolecule
 * User: Steve
 * Date: 7/11/12
 */
public class AsaMolecule {
    public static final AsaMolecule[] EMPTY_ARRAY = {};

    public static AsaMolecule fromPDB(File pdb) {
        String[] lines = FileUtilities.readInLines(pdb);
        return fromPDB(lines);
    }

    public static AsaMolecule fromPDB(String[] lines) {
        AsaMolecule ret = new AsaMolecule();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("ATOM") || line.startsWith("HETATM")) {
                try {
                    ret.handleAtom(line);
                }
                catch (UnknownAtomException e) {
                    continue;
                }
                catch (NumberFormatException e) {
                    continue;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    AsaAtom added = new AsaAtom(line); // try again
                }
            }
            if (line.startsWith("ENDMDL"))
                return ret;
        }
        return ret;
    }


     private final Map<String, AsaSubunit> m_Subunits = new HashMap<String, AsaSubunit>();

    public AsaSubunit[] getSubunits() {
        AsaSubunit[] ret = m_Subunits.values().toArray(AsaSubunit.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }


    public AsaSubunit[] getAccessibleSubunits() {
        List<AsaSubunit> holder = new ArrayList<AsaSubunit>();
        for (AsaSubunit su : getSubunits()) {
            if (su.isAccessible())
                holder.add(su);
        }
        AsaSubunit[] ret = new AsaSubunit[holder.size()];
        holder.toArray(ret);
        return ret;
    }
    protected final void handleAtom(String line) {
        buildAtom(line);
    }

    protected AsaAtom buildAtom(String line) {
        AsaAtom added = new AsaAtom(line);
        addAtom(added);
        return added;
    }

    public AsaSubunit[] getInaccessibleSubunits() {
        List<AsaSubunit> holder = new ArrayList<AsaSubunit>();
        for (AsaSubunit su : getSubunits()) {
            if (!su.isAccessible())
                holder.add(su);
        }
        AsaSubunit[] ret = new AsaSubunit[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public AsaSubunit getSubunit(AsaAtom atom) {
        return getSubunit(atom.getChainId(), atom.getResType(), atom.getResNum());
    }


    public AsaSubunit getSubunit(ChainEnum chainId, String resType, int resNum) {
        String id = AsaSubunit.buildSubUnitString(chainId.toString(), resType, resNum);
        AsaSubunit asaSubunit = m_Subunits.get(id);
        if (asaSubunit == null) {
            FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(resType);
            if(aa == null)
                return null;
            asaSubunit = buildSubunit(chainId, resType, resNum );
            addSubUnit(id, asaSubunit);
        }
        return asaSubunit;
    }

    protected void addSubUnit(final String id, final AsaSubunit asaSubunit) {
        m_Subunits.put(id, asaSubunit);
    }

    protected AsaSubunit buildSubunit(  ChainEnum chainId,   String resType,   int resNum ) {
        String id = AsaSubunit.buildSubUnitString(chainId.toString(), resType, resNum);
        final AsaSubunit asaSubunit;
        asaSubunit = new AsaSubunit(chainId, resType, resNum);
        return asaSubunit;
    }

//    public int n_atom() {
//        return m_Atoms.size();
//    }
//
//    public AsaAtom atom(int i) {
//        return m_Atoms.get(i);
//    }
//
//    public AsaAtom[] getAtoms() {
//        return m_Atoms.toArray(AsaAtom.EMPTY_ARRAY);
//    }
//
//    public void clear() {
//        m_Atoms.clear();
//    }
//public void transform(Matrix3d mx) {
//    for (AsaAtom atom : m_Atoms)
//        atom.transform(mx);
//
//}
//
//public void erase_atom(String atom_type) {
//    List<AsaAtom> holder = new ArrayList<AsaAtom>();
//
//    for (AsaAtom atom : m_Atoms) {
//        if (atom.getType().equals(atom_type))
//            holder.add(atom);
//    }
//    m_Atoms.removeAll(holder);
//}


    public AsaAtom[] getAtoms() {
       List<AsaAtom> holder = new ArrayList<AsaAtom>();
       accumulateAtoms(holder);
       AsaAtom[] ret = new AsaAtom[holder.size()];
       holder.toArray(ret);
       return ret;
    }


    public void accumulateAtoms(Collection<AsaAtom> c) {
        for( AsaSubunit su : getSubunits() )
            su.accumulateAtoms(c);
    }


    public void addAtom(AsaAtom added) {
//        m_Atoms.add(added);
//        if("HOH".equals(added.getResType()))
//            return; // do not add water
        AsaSubunit subunit = getSubunit(added);
        if(subunit != null)
            subunit.addAtom(added);
    }


    public String asPDB() {
        StringBuilder sb = new StringBuilder();
        AsaAtom[] atoms = getAtoms();
        Arrays.sort(atoms);
        for (AsaAtom atom : atoms) {
            sb.append(atom.asPDB());
        }
        return sb.toString();
    }


    /*
   class Molecule:

    def __init__(self, pdb=""):
      self.id = ''
      self._atoms = []
      if pdb:
        self.read_pdb(pdb)

    def n_atom(self):
      return len(self._atoms)

    def atoms(self):
      return self._atoms

    def atom(self, i):
      return _atoms[i]

    def clear(self):
      for atom in self._atoms:
        del atom
      del self._atoms[:]

    def transform(self, matrix):
      for atom in self._atoms:
        atom.pos.transform(matrix)

    def addAtom(self, atom):
      self._atoms.append(atom)

    def erase_atom(self, atom_type):
      for atom in self._atoms:
        if atom.type == atom_type:
          self._atoms.remove(atom)
          del atom
          return

    def read_pdb(self, fname):
      self.clear()
      for line in open(fname, 'r').readlines():
        if line.startswith("ATOM") or line.startswith("HETATM"):
          atom = AtomFromPdbLine(line);
          if len(self._atoms) == 1:
            self.id = atom.chain_id
          self.addAtom(atom)
        if line.startswith("ENDMDL"):
          return

    def write_pdb(self, pdb):
      f = open(pdb, 'w')
      n_atom = 0
      for atom in sorted(self._atoms, cmp=cmp_atom):
        n_atom += 1
        atom.num = n_atom
        f.write(atom.pdb_str() + '\n')
      f.close()
    */
}
