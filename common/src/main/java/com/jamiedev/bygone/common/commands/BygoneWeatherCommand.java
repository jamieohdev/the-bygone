package com.jamiedev.bygone.common.commands;

import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.common.weather.weather_types.WeatherProperties;
import com.jamiedev.bygone.common.weather.weather_types.WeatherType;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BygoneWeatherCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("bygone_weather")
            .requires(stack -> stack.hasPermission(2))
            .then(
                Commands.argument("weather_type", ResourceArgument.resource(context, BygoneWeather.WEATHER_TYPE_REGISTRY_KEY))
                    .then(Commands.literal("start").executes(BygoneWeatherCommand::startWeather))
                    .then(Commands.literal("clear").executes(BygoneWeatherCommand::clearWeather))
                    .then(Commands.literal("property").then(
                        Commands.argument("property", StringArgumentType.word())
                            .suggests((context1, builder) -> {
                                Optional<WeatherType> weatherType = getWeatherType(context1);
                                if (weatherType.isPresent()) {
                                    Set<String> propertySet = weatherType.get().getProperties()
                                        .stream().map(WeatherProperties.WeatherProperty::getIdentifier)
                                        .collect(Collectors.toSet());
                                    for (String property : propertySet)
                                        builder.suggest(property);
                                }
                                return builder.buildFuture();
                            }).then(Commands.literal("query").executes(BygoneWeatherCommand::queryPropertyValue))
                              .then(Commands.literal("set").then(
                                  Commands.argument("value", StringArgumentType.word())
                                      .executes(BygoneWeatherCommand::setPropertyValue)))
                        )
                    )
            )
        );
    }

    public static <T> Optional<WeatherProperties.WeatherProperty<T>> getProperty(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        Optional<WeatherType> weatherType = getWeatherType(stackCommandContext);
        if (weatherType.isEmpty()) return Optional.empty();
        String propertyName = StringArgumentType.getString(stackCommandContext, "property");
        return Optional.of(weatherType.get().getProperty(propertyName));
    }

    public static int queryPropertyValue(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        getProperty(stackCommandContext).ifPresent((property) -> {
            try {
                String weatherName = ResourceArgument.getResource(stackCommandContext, "weather_type", BygoneWeather.WEATHER_TYPE_REGISTRY_KEY)
                    .getRegisteredName();
                stackCommandContext.getSource().sendSuccess(() -> Component.translatable(
                    "commands.bygone.weather.query_property", property.getIdentifier(), weatherName, property.getValue().toString()
                ), true);
            } catch (CommandSyntaxException e) {throw new RuntimeException(e);}
        });
        return 1;
    }

    private static final DynamicCommandExceptionType INVALID_CAST_EXCEPTION = new DynamicCommandExceptionType(input
                -> Component.translatable("commands.bygone.weather.incompatible_cast", input));
    private static final SimpleCommandExceptionType BYGONE_LEVEL_NOT_LOADED_EXCEPTION = new SimpleCommandExceptionType(
        Component.translatable("commands.bygone.weather.dimension_not_loaded")
    );

    public static <T> void attemptSetValue(WeatherProperties.WeatherProperty<T> property, String propertyValue) throws CommandSyntaxException {
        Class<T> valueClass = (Class<T>) property.getValue().getClass();
        try {
            java.lang.reflect.Method method = valueClass.getMethod("valueOf", String.class);
            property.setValue(valueClass.cast(method.invoke(null, propertyValue)));
        } catch (Exception e) {
            throw INVALID_CAST_EXCEPTION.create(valueClass.toString());
        }
    }

    public static int setPropertyValue(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        // property value? sure hope it does
        String propertyValue = StringArgumentType.getString(stackCommandContext, "value");
        Optional<WeatherProperties.WeatherProperty<Object>> optionalWeather = getProperty(stackCommandContext);
        if (optionalWeather.isEmpty()) return 0;

        WeatherProperties.WeatherProperty<?> property = optionalWeather.get();
        String previousProperty = property.getValue().toString();
        String weatherName = ResourceArgument
            .getResource(stackCommandContext, "weather_type", BygoneWeather.WEATHER_TYPE_REGISTRY_KEY)
            .getRegisteredName();

        attemptSetValue(property, propertyValue);

        stackCommandContext.getSource().sendSuccess(() -> Component.translatable(
            "commands.bygone.weather.set_property",
            property.getIdentifier(), weatherName,
            property.getValue().toString(), previousProperty
        ), true);
        return 1;
    }

    @SuppressWarnings("rawtypes")
    private static Optional<WeatherType> getWeatherType(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        BygoneWeather weatherSystem = getWeatherSystem(stackCommandContext);
        Holder.Reference<WeatherType.Factory> factoryReference = ResourceArgument.getResource(stackCommandContext,
            "weather_type", BygoneWeather.WEATHER_TYPE_REGISTRY_KEY);
        return weatherSystem.getWeatherType(factoryReference.value().getLocation());
    }

    private static BygoneWeather getWeatherSystem(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        ServerLevel bygoneLevel = stackCommandContext.getSource().getServer().getLevel(BGDimensions.BYGONE_LEVEL_KEY);
        if (bygoneLevel == null) throw BYGONE_LEVEL_NOT_LOADED_EXCEPTION.create();
        return BygoneWeather.getOrDefault(bygoneLevel);
    }

    public static int startWeather(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        Optional<WeatherType> weatherType = getWeatherType(stackCommandContext);
        if (weatherType.isEmpty()) return 0;

        weatherType.get().startWeather();

        String weatherName = ResourceArgument
            .getResource(stackCommandContext, "weather_type", BygoneWeather.WEATHER_TYPE_REGISTRY_KEY)
            .getRegisteredName();
        stackCommandContext.getSource().sendSuccess(
            () -> Component.translatable("commands.bygone.weather.start", weatherName), true);
        return 1;
    }

    public static int clearWeather(CommandContext<CommandSourceStack> stackCommandContext) throws CommandSyntaxException {
        Optional<WeatherType> weatherType = getWeatherType(stackCommandContext);
        if (weatherType.isEmpty()) return 0;

        weatherType.get().clearWeather();

        String weatherName = ResourceArgument
            .getResource(stackCommandContext, "weather_type", BygoneWeather.WEATHER_TYPE_REGISTRY_KEY)
            .getRegisteredName();
        stackCommandContext.getSource().sendSuccess(
            () -> Component.translatable("commands.bygone.weather.clear", weatherName), true);
        return 1;
    }
}
