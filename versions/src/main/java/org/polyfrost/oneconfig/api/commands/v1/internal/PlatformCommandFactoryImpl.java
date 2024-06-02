/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2024 Polyfrost.
 *   <https://polyfrost.org> <https://github.com/Polyfrost/>
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   OneConfig is licensed under the terms of version 3 of the GNU Lesser
 * General Public License as published by the Free Software Foundation, AND
 * under the Additional Terms Applicable to OneConfig, as published by Polyfrost,
 * either version 1.0 of the Additional Terms, or (at your option) any later
 * version.
 *
 *   This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 * License.  If not, see <https://www.gnu.org/licenses/>. You should
 * have also received a copy of the Additional Terms Applicable
 * to OneConfig, as published by Polyfrost. If not, see
 * <https://polyfrost.org/legal/oneconfig/additional-terms>
 */

package org.polyfrost.oneconfig.api.commands.v1.internal;

import net.minecraft.command.CommandBase;
import net.minecraft.util.BlockPos;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.commands.v1.exceptions.CommandExecutionException;
import org.polyfrost.oneconfig.api.commands.v1.CommandTree;
import org.polyfrost.oneconfig.api.commands.v1.factories.PlatformCommandFactory;
import org.polyfrost.oneconfig.api.ClassHasOverwrites;
import org.polyfrost.universal.UChat;
import org.polyfrost.oneconfig.api.commands.v1.arguments.PlayerArgumentParser;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;

//#if FORGE
import net.minecraftforge.client.ClientCommandHandler;
//#endif

@ClassHasOverwrites("1.16.5-forge")
public class PlatformCommandFactoryImpl implements PlatformCommandFactory {
    private static final Logger LOGGER = LogManager.getLogger("OneConfig/Commands");

    static {
        CommandManager.INSTANCE.registerParser(new PlayerArgumentParser());
    }

    @Override
    public boolean createCommand(CommandTree tree) {
        ClientCommandHandler.instance.registerCommand(new CommandBase() {
            @Override
            public String getCommandName() {
                return tree.name();
            }

            @Override
            public String getCommandUsage(net.minecraft.command.ICommandSender sender) {
                return "/" + tree.name();
            }

            @Override
            public void
            //#if MC<=10809
            processCommand(net.minecraft.command.ICommandSender sender, String[] args)
            //#elseif FABRIC
            //$$ method_3279(net.minecraft.server.MinecraftServer var1, net.minecraft.command.CommandSource sender, String[] args)
            //#else
            //$$ execute(net.minecraft.server.MinecraftServer server, net.minecraft.command.ICommandSender sender, String[] args)
            //#endif
            {
                if (args.length == 1 && args[0].equals("help")) {
                    for (String s : tree.getHelp()) {
                        chat(s);
                    }
                    return;
                }
                try {
                    Object out = tree.execute(args);
                    if (out == null) return;
                    if (out.getClass().isArray()) {
                        for (Object o : (Object[]) out) {
                            chat(o.toString());
                        }
                    } else {
                        chat(out.toString());
                    }
                } catch (CommandExecutionException c) {
                    chat("&c" + c.getMessage());
                    LOGGER.warn(c.getMessage());
                } catch (Exception e) {
                    chat("&cAn unknown error occurred while executing this command, please report this to the mod author!");
                    LOGGER.error("failed to run command method", e);
                }
            }

            @Override
            public List<String> getCommandAliases() {
                return Arrays.asList(tree.names());
            }

            @Override
            public int getRequiredPermissionLevel() {
                return -1;
            }

            @Override
            public List<String>
            //#if MC<=10809
            addTabCompletionOptions(net.minecraft.command.ICommandSender sender, String[] args, BlockPos pos)
            //#elseif FABRIC
            //$$ method_10738(net.minecraft.server.MinecraftServer server, net.minecraft.command.CommandSource sender, String[] args, BlockPos targetPos)
            //#else
            //$$ getTabCompletions(net.minecraft.server.MinecraftServer server, net.minecraft.command.ICommandSender sender, String[] args, BlockPos targetPos)
            //#endif
            {
                return tree.autocomplete(args);
            }
        });
        return true;
    }

    private static void chat(String s) {
        UChat.chat(s);
    }
}