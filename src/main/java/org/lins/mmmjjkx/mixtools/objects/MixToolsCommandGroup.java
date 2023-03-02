package org.lins.mmmjjkx.mixtools.objects;

import java.util.List;

public class MixToolsCommandGroup {
    private final String name;

    private final List<String> commands;

    public MixToolsCommandGroup(String name, List<String> commands){
        this.name = name;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public List<String> getCommands() {
        return commands;
    }
}
