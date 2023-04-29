package org.lins.mmmjjkx.mixtools.objects.records;

import java.util.List;

public record MixToolsScheduler(String name, long delay, List<String> actions, boolean manuallyStart) {}
