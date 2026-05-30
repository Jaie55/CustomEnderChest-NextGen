# Graph Report - .  (2026-05-30)

## Corpus Check
- 40 files · ~50,492 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 351 nodes · 825 edges · 16 communities (12 shown, 4 thin omitted)
- Extraction: 55% EXTRACTED · 45% INFERRED · 0% AMBIGUOUS · INFERRED: 373 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 15|Community 15]]

## God Nodes (most connected - your core abstractions)
1. `Scheduler` - 24 edges
2. `StorageInterface` - 23 edges
3. `EnderChestManager` - 18 edges
4. `H2Storage` - 18 edges
5. `MySQLStorage` - 18 edges
6. `YmlStorage` - 18 edges
7. `EnderChestCommand` - 17 edges
8. `StorageManager` - 16 edges
9. `PlayerListener` - 13 edges
10. `BackupManager` - 12 edges

## Surprising Connections (you probably didn't know these)
- `Auto + Shutdown Backup System` --protects--> `Async Data Persistence`  [INFERRED]
  src/main/java/org/maiminhdung/customenderchest/backup/BackupManager.java → AI_CONTEXT.md
- `Folia/Canvas/Bukkit Compatibility` --enables--> `Async Data Persistence`  [INFERRED]
  src/main/java/org/maiminhdung/customenderchest/Scheduler.java → AI_CONTEXT.md
- `Async Data Persistence` --requires--> `Per-Player Data Locking`  [INFERRED]
  AI_CONTEXT.md → src/main/java/org/maiminhdung/customenderchest/utils/DataLockManager.java
- `Paper Byte-Based Item Serialization` --supports--> `Async Data Persistence`  [EXTRACTED]
  src/main/java/org/maiminhdung/customenderchest/data/ItemSerializer.java → AI_CONTEXT.md
- `README Project Overview` --references--> `Folia/Canvas/Bukkit Compatibility`  [EXTRACTED]
  README.md → src/main/java/org/maiminhdung/customenderchest/Scheduler.java

## Hyperedges (group relationships)
- **Data Integrity Protection Trio** — concept_data_locking, concept_async_persistence, concept_backup_system [INFERRED 0.85]
- **Storage Layer Architecture** — concept_hikaricp_pooling, concept_item_serialization, concept_async_persistence [EXTRACTED 1.00]
- **User Experience Layer** — concept_minimessage_text, concept_locale_system, concept_permission_system [INFERRED 0.80]

## Communities (16 total, 4 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.10
Nodes (4): Scheduler, EnderChestManager, DataLockManager, EnderChestUtils

### Community 2 - "Community 2"
Cohesion: 0.08
Nodes (9): AdvancedBarChart, AdvancedPie, CustomChart, DrilldownPie, Metrics, MultiLineChart, SimpleBarChart, SimplePie (+1 more)

### Community 3 - "Community 3"
Cohesion: 0.09
Nodes (5): ConfigHandler, Task, Listener, PlayerListener, SoundHandler

### Community 4 - "Community 4"
Cohesion: 0.11
Nodes (3): BackupManager, StorageManager, DebugLogger

### Community 5 - "Community 5"
Cohesion: 0.14
Nodes (5): CommandExecutor, EnderChestCommand, LocaleManager, MigrationManager, TabCompleter

### Community 6 - "Community 6"
Cohesion: 0.13
Nodes (3): ItemSerializer, YmlStorage, Text

### Community 7 - "Community 7"
Cohesion: 0.09
Nodes (3): PlayerDataInfo, StorageInterface, StorageStats

### Community 8 - "Community 8"
Cohesion: 0.15
Nodes (3): EnderChest, MetricsDataProvider, JavaPlugin

### Community 9 - "Community 9"
Cohesion: 0.16
Nodes (18): AI Context Architecture Doc, Admin Chest View/Edit, Async Data Persistence, Auto + Shutdown Backup System, Per-Player Data Locking, Folia/Canvas/Bukkit Compatibility, Guava Cache for Live Data, HikariCP Connection Pooling (+10 more)

### Community 10 - "Community 10"
Cohesion: 0.21
Nodes (3): BulkImporter, PlayerData, NbtImporter

## Knowledge Gaps
- **4 isolated node(s):** `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `Plugin YAML Metadata`, `Gradle Build CI Pipeline`
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `StorageInterface` connect `Community 7` to `Community 1`, `Community 11`, `Community 5`, `Community 6`?**
  _High betweenness centrality (0.116) - this node is a cross-community bridge._
- **Why does `Scheduler` connect `Community 0` to `Community 1`, `Community 3`, `Community 4`?**
  _High betweenness centrality (0.055) - this node is a cross-community bridge._
- **What connects `java.compile.nullAnalysis.mode`, `java.configuration.updateBuildConfiguration`, `Plugin YAML Metadata` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.09585037989479836 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.13842746400885936 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.08067226890756303 - nodes in this community are weakly interconnected._
- **Should `Community 3` be split into smaller, more focused modules?**
  _Cohesion score 0.0855614973262032 - nodes in this community are weakly interconnected._