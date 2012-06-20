package org.systemsbiology.gatk;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.VariantReader
 * User: steven
 * Date: 6/12/12
 */
public class VariantReader {
    public static final VariantReader[] EMPTY_ARRAY = {};

    /**
     * annotatedGeneFile then vcf file
     *
     * @param args
     */
    public static void main(String[] args) {
        String geneFile = args[0];
        String vcfFile = args[1];
        MapToGeneRegion map = GeneUtilities.readGeneMap(geneFile);
        final String[] lines;
        lines = GeneUtilities.readInLines(vcfFile);
        GeneVariant[] variants = GeneUtilities.variantsFromLines(lines);
        for (int i = 0; i < variants.length; i++) {
            GeneVariant variant = variants[i];
            map.addVariant(variant);
        }

        RegionWithVariants[] regionsWithVariants = map.getRegionsWithVariants();
        for (int i = 0; i < regionsWithVariants.length; i++) {
            RegionWithVariants r = regionsWithVariants[i];
            GeneRegion region = r.getRegion();
            GeneVariant[] variants1 = r.getVariants();
            System.out.print(region.getDescription() + "\t" + region.getInterval() + "\t");
            for (int j = 0; j < variants1.length; j++) {
                GeneVariant geneVariant = variants1[j];
                if (j > 0)
                    System.out.print(",");
                System.out.print(geneVariant);
            }
            System.out.println();
        }

    }


}
