package com.github.unchama.seichiassist.itemmigration

import cats.effect.IO
import com.github.unchama.itemmigration.ItemMigration.ItemConversion
import com.github.unchama.itemmigration.ItemMigrationTarget
import com.github.unchama.itemmigration.target.WorldLevelData
import com.github.unchama.util.external.{ExternalPlugins, ExternalServices}
import org.bukkit.World

object SeichiAssistWorldLevelData extends ItemMigrationTarget[IO] {

  val getWorlds: IO[IndexedSeq[World]] = {
    val multiverseCore = ExternalPlugins.getMultiverseCore

    import scala.jdk.CollectionConverters._

    IO {
      multiverseCore.getMVWorldManager
        .getMVWorlds.asScala
        .map(_.getCBWorld).toIndexedSeq
    }
  }

  val getWorldChunkCoordinates: World => IO[Seq[(Int, Int)]] = {
    val command = ExternalServices.defaultCommand

    ExternalServices.getChunkCoordinates(command)
  }

  override def runMigration(conversion: ItemConversion): IO[Unit] =
    WorldLevelData(getWorlds, getWorldChunkCoordinates).runMigration(conversion)
}
