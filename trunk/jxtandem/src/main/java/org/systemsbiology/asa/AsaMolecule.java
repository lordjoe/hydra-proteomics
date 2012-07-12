package org.systemsbiology.asa;

import java.util.*;

/**
 * org.systemsbiology.asa.AsaMolecule
 * User: Steve
 * Date: 7/11/12
 */
public class AsaMolecule {
    public static final AsaMolecule[] EMPTY_ARRAY = {};

    private final List<AsaAtom>  m_Atoms = new ArrayList<AsaAtom>();

    public int n_atom() {
         return m_Atoms.size();
      }

    public AsaAtom atom(int i) {
          return m_Atoms.get(i);
       }

    public AsaAtom[] getAtoms( ) {
          return m_Atoms.toArray(AsaAtom.EMPTY_ARRAY);
       }


    public void clear( ) {
           m_Atoms.clear( );
      }

    public void insert_atom(AsaAtom added ) {
           m_Atoms.add(added );
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

  def insert_atom(self, atom):
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
        self.insert_atom(atom)
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
