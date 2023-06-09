package net.minestom.codegen.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.javapoet.*;
import net.minestom.codegen.MinestomCodeGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

// Rainstom
public class SoundTypeGenerator extends MinestomCodeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundTypeGenerator.class);
    private final InputStream soundTypesFile;
    private final File outputFolder;

    public SoundTypeGenerator(InputStream soundTypesFile, File outputFolder) {
        this.soundTypesFile = soundTypesFile;
        this.outputFolder = outputFolder;
    }

    @Override
    public void generate() {
        if (soundTypesFile == null) {
            LOGGER.error("Failed to find sound_types.json.");
            LOGGER.error("Stopped code generation for sound types.");
            return;
        }
        if (!outputFolder.exists() && !outputFolder.mkdirs()) {
            LOGGER.error("Output folder for code generation does not exist and could not be created.");
            return;
        }
        final var soundTypes = GSON.fromJson(new InputStreamReader(soundTypesFile), JsonObject.class);
        final var soundEventCN = ClassName.get("net.minestom.server.sound", "SoundEvent");
        final var stringCN = ClassName.get("java.lang", "String");
        final var soundTypeCN = ClassName.get("dev.uten2c.rainstom.block", "SoundType");
        final var enumBuilder = TypeSpec.enumBuilder(soundTypeCN)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("AUTOGENERATED by " + getClass().getSimpleName());

        enumBuilder.addFields(
                List.of(
                        FieldSpec.builder(TypeName.FLOAT, "volume", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(TypeName.FLOAT, "pitch", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(soundEventCN, "breakSound", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(soundEventCN, "stepSound", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(soundEventCN, "placeSound", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(soundEventCN, "hitSound", Modifier.PRIVATE, Modifier.FINAL).build(),
                        FieldSpec.builder(soundEventCN, "fallSound", Modifier.PRIVATE, Modifier.FINAL).build()
                )
        );

        final var notNullStringCN = stringCN.annotated(AnnotationSpec.builder(NotNull.class).build());
        final var notNullSoundEventCN = soundEventCN.annotated(AnnotationSpec.builder(NotNull.class).build());
        enumBuilder.addMethods(
                List.of(
                        MethodSpec.constructorBuilder()
                                .addParameters(
                                        List.of(
                                                ParameterSpec.builder(TypeName.FLOAT, "volume").build(),
                                                ParameterSpec.builder(TypeName.FLOAT, "pitch").build(),
                                                ParameterSpec.builder(notNullStringCN, "breakSound").build(),
                                                ParameterSpec.builder(notNullStringCN, "stepSound").build(),
                                                ParameterSpec.builder(notNullStringCN, "placeSound").build(),
                                                ParameterSpec.builder(notNullStringCN, "hitSound").build(),
                                                ParameterSpec.builder(notNullStringCN, "fallSound").build()
                                        )
                                )
                                .addStatement("this.volume = volume")
                                .addStatement("this.pitch = pitch")
                                .addStatement("this.breakSound = $T.fromNamespaceId(breakSound)", soundEventCN)
                                .addStatement("this.stepSound = $T.fromNamespaceId(stepSound)", soundEventCN)
                                .addStatement("this.placeSound = $T.fromNamespaceId(placeSound)", soundEventCN)
                                .addStatement("this.hitSound = $T.fromNamespaceId(hitSound)", soundEventCN)
                                .addStatement("this.fallSound = $T.fromNamespaceId(fallSound)", soundEventCN)
                                .build(),
                        MethodSpec.methodBuilder("getVolume")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.FLOAT)
                                .addStatement("return this.volume")
                                .build(),
                        MethodSpec.methodBuilder("getPitch")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.FLOAT)
                                .addStatement("return this.pitch")
                                .build(),
                        MethodSpec.methodBuilder("getBreakSound")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(notNullSoundEventCN)
                                .addStatement("return this.breakSound")
                                .build(),
                        MethodSpec.methodBuilder("getStepSound")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(notNullSoundEventCN)
                                .addStatement("return this.stepSound")
                                .build(),
                        MethodSpec.methodBuilder("getPlaceSound")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(notNullSoundEventCN)
                                .addStatement("return this.placeSound")
                                .build(),
                        MethodSpec.methodBuilder("getHitSound")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(notNullSoundEventCN)
                                .addStatement("return this.hitSound")
                                .build(),
                        MethodSpec.methodBuilder("getFallSound")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(notNullSoundEventCN)
                                .addStatement("return this.fallSound")
                                .build()
                )
        );

        for (Map.Entry<String, JsonElement> entry : soundTypes.entrySet()) {
            final var name = entry.getKey();
            final var jsonObject = entry.getValue().getAsJsonObject();
            enumBuilder.addEnumConstant(name, TypeSpec.anonymousClassBuilder(
                    "$Lf, $Lf, $S, $S, $S, $S, $S",
                    jsonObject.get("volume").getAsFloat(),
                    jsonObject.get("pitch").getAsFloat(),
                    jsonObject.get("breakSound").getAsString(),
                    jsonObject.get("stepSound").getAsString(),
                    jsonObject.get("placeSound").getAsString(),
                    jsonObject.get("hitSound").getAsString(),
                    jsonObject.get("fallSound").getAsString()
            ).build());
        }

        writeFiles(
                List.of(
                        JavaFile.builder("dev.uten2c.rainstom.block", enumBuilder.build())
                                .indent("    ")
                                .skipJavaLangImports(true)
                                .build()
                ),
                outputFolder
        );
    }
}
