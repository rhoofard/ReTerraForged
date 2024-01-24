package raccoonman.reterraforged.data.export.preset;

import net.minecraft.util.CubicSpline;
import net.minecraft.util.ToFloatFunction;

// this is only for reference because the original TerrainProvider class is impossible to read
class VanillaTerrainProvider {

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> overworldFactor(I continentalness, I erosion, I ridges, I peaksAndValleys) {
        return CubicSpline.builder(continentalness)
        	.addPoint(-0.19F, 3.95F)
        	.addPoint(-0.15F, getErosionFactor(erosion, ridges, peaksAndValleys, 6.25F, true))
        	.addPoint(-0.1F, getErosionFactor(erosion, ridges, peaksAndValleys, 5.47F, true))
        	.addPoint(0.03F, getErosionFactor(erosion, ridges, peaksAndValleys, 5.08F, true))
        	.addPoint(0.06F, getErosionFactor(erosion, ridges, peaksAndValleys, 4.69F, false)).build();
    }
    
    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I erosion, I ridges, I peaksAndValleys, float baseFactor, boolean nonFarInland) {
        CubicSpline<C, I> baseSpline = CubicSpline.builder(ridges)
        	.addPoint(-0.2F, 6.3F)
        	.addPoint(0.2F, baseFactor)
        	.build();
        CubicSpline.Builder<C, I> erosionSpline = CubicSpline.builder(erosion)
        	.addPoint(-0.6F, baseSpline)
        	.addPoint(-0.5F, CubicSpline.builder(ridges)
        		.addPoint(-0.05F, 6.3F).addPoint(0.05F, 2.67F)
        		.build()
        	)
        	.addPoint(-0.35F, baseSpline)
        	.addPoint(-0.25F, baseSpline)
        	.addPoint(-0.1F, CubicSpline.builder(ridges)
        		.addPoint(-0.05F, 2.67F)
        		.addPoint(0.05F, 6.3F)
        		.build()
        	)
        	.addPoint(0.03F, baseSpline);
        if (nonFarInland) {
            CubicSpline<C, I> cubicSpline2 = CubicSpline.builder(ridges)
            	.addPoint(0.0F, baseFactor)
            	.addPoint(0.1F, 0.625F)
            	.build();
            CubicSpline<C, I> cubicSpline3 = CubicSpline.builder(peaksAndValleys)
            	.addPoint(-0.9F, baseFactor)
            	.addPoint(-0.69F, cubicSpline2)
            	.build();
            erosionSpline.addPoint(0.35F, baseFactor)
            	   .addPoint(0.45F, cubicSpline3)
            	   .addPoint(0.55F, cubicSpline3)
            	   .addPoint(0.62F, baseFactor);
        } else {
            CubicSpline<C, I> cubicSpline2 = CubicSpline.builder(peaksAndValleys)
            	.addPoint(-0.7F, baseSpline)
            	.addPoint(-0.15F, 1.37F)
            	.build();
            CubicSpline<C, I> cubicSpline3 = CubicSpline.builder(peaksAndValleys)
            	.addPoint(0.45F, baseSpline)
            	.addPoint(0.7F, 1.56F)
            	.build();
            erosionSpline.addPoint(0.05F, cubicSpline3)
            	   .addPoint(0.4F, cubicSpline3)
            	   .addPoint(0.45F, cubicSpline2)
            	   .addPoint(0.55F, cubicSpline2)
            	   .addPoint(0.58F, baseFactor);
        }
        return erosionSpline.build();
    }

}
