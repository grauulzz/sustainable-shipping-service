package com.amazon.ata.types;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.amazon.ata.types.Material.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaterialTest {
    @Test
    public void whenStillFailingJacoco_material_tryingToFindMoreCodeTOTest1() {

        List<Material> lm = Arrays.stream(values()).filter(material -> material
                        .equals(LAMINATED_PLASTIC) || material.equals(CORRUGATE))
                .collect(Collectors.toList());
        assertTrue(lm.contains(LAMINATED_PLASTIC) && lm.contains(CORRUGATE));
    }
    @Test
    public void whenStillFailingJacoco_material_tryingToFindMoreCodeTOTest2() {

        List<Material> lm = Arrays.stream(values()).filter(material -> material
                        .equals(LAMINATED_PLASTIC) || material.equals(CORRUGATE))
                .collect(Collectors.toList());

        assertEquals(values().length, lm.size());
    }
}
