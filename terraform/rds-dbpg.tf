resource "aws_db_parameter_group" "mindlevel-mariadb" {
    name        = "mindlevel-mariadb"
    family      = "mariadb10.2"
    description = "UTF-8 with emojis etc"

    parameter {
        name         = "aria_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_checkpoint_interval"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_checkpoint_log_activity"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_encrypt_tables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_force_start_after_recovery_failures"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_group_commit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_group_commit_interval"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_log_file_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_log_purge_type"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_max_sort_file_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_page_checksum"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_pagecache_age_threshold"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_pagecache_division_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_recover_options"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_repair_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_sort_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_stats_method"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "aria_sync_log_dir"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "auto_increment_increment"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "auto_increment_offset"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "autocommit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "automatic_sp_privileges"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "back_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "basedir"
        value        = "/rdsdbbin/mysql"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_annotate_row_events"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_cache_size"
        value        = "32768"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_checksum"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_commit_wait_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_commit_wait_usec"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_direct_non_transactional_updates"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_format"
        value        = "MIXED"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_row_image"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "binlog_stmt_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "bulk_insert_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "character_set_client"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "character_set_connection"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "character_set_database"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "character_set_filesystem"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "character_set_results"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "character_set_server"
        value        = "utf8mb4"
        apply_method = "immediate"
    }

    parameter {
        name         = "check_constraint_checks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "collation_connection"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "collation_server"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "completion_type"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "concurrent_insert"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "connect_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "datadir"
        value        = "/rdsdbdata/db/"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "deadlock_search_depth_long"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "deadlock_search_depth_short"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "deadlock_timeout_long"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "deadlock_timeout_short"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "default_regex_flags"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "default_storage_engine"
        value        = "InnoDB"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "default_time_zone"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "default_tmp_storage_engine"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "default_week_format"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "delay_key_write"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "delayed_insert_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "delayed_insert_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "delayed_queue_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "div_precision_increment"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "encrypt_binlog"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "encrypt_tmp_disk_tables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "encrypt_tmp_files"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "event_scheduler"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "expensive_subquery_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "expire_logs_days"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "explicit_defaults_for_timestamp"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "extra_max_connections"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "extra_port"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "flush"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "flush_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "foreign_key_checks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "ft_boolean_syntax"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "ft_max_word_len"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "ft_min_word_len"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "ft_query_expansion_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "ft_stopword_file"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "general_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "general_log_file"
        value        = "/rdsdbdata/log/general/mysql-general.log"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "group_concat_max_len"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "gtid_domain_id"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "gtid_strict_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "histogram_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "histogram_type"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "host_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "init_connect"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_flushing"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_flushing_lwm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_hash_index"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_hash_index_partitions"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_hash_index_parts"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_adaptive_max_sleep_delay"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_autoextend_increment"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_autoinc_lock_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_background_scrub_data_check_interval"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_background_scrub_data_compressed"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_background_scrub_data_interval"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_background_scrub_data_uncompressed"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buf_dump_status_frequency"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_dump_at_shutdown"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_dump_now"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_dump_pct"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_filename"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_load_abort"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_load_at_startup"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_load_now"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_populate"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_buffer_pool_size"
        value        = "{DBInstanceClassMemory*3/4}"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_change_buffer_max_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_change_buffering"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_checksum_algorithm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_cleaner_lsn_age_factor"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_cmp_per_index_enabled"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_commit_concurrency"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_compression_algorithm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_compression_default"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_compression_failure_threshold_pct"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_compression_level"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_compression_pad_pct_max"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_concurrency_tickets"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_corrupt_table_action"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_data_home_dir"
        value        = "/rdsdbdata/db/innodb"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_deadlock_detect"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_default_encryption_key_id"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_default_row_format"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment_fill_factor"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment_fill_factor_n_recs"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment_frequency"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment_n_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_defragment_stats_accuracy"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_disable_sort_file_cache"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_disallow_writes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_empty_free_list_algorithm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_encrypt_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_encrypt_tables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_encryption_rotate_key_age"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_encryption_rotation_iops"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_encryption_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_fake_changes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_fast_shutdown"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_fatal_semaphore_wait_threshold"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_file_format"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_file_format_max"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_file_per_table"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_fill_factor"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flush_log_at_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flush_log_at_trx_commit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flush_method"
        value        = "O_DIRECT"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flush_neighbors"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flush_sync"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_flushing_avg_loops"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_force_load_corrupted"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_force_primary_key"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_foreground_preflush"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_aux_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_enable_diag_print"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_enable_stopword"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_max_token_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_min_token_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_num_word_optimize"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_result_cache_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_server_stopword_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_sort_pll_degree"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_ft_user_stopword_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_idle_flush_pct"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_immediate_scrub_data_uncompressed"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_instrument_semaphores"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_io_capacity"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_io_capacity_max"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_kill_idle_transaction"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_large_prefix"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_lock_schedule_algorithm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_lock_wait_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_locking_fake_changes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_arch_dir"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_arch_expire_sec"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_archive"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_buffer_size"
        value        = "8388608"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_checksum_algorithm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_checksums"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_compressed_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_file_size"
        value        = "134217728"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_group_home_dir"
        value        = "/rdsdbdata/log/innodb"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_log_write_ahead_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_lru_scan_depth"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_bitmap_file_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_changed_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_dirty_pages_pct"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_dirty_pages_pct_lwm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_purge_lag"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_purge_lag_delay"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_max_undo_log_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_monitor_disable"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_monitor_enable"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_monitor_reset"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_monitor_reset_all"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_mtflush_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_old_blocks_pct"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_old_blocks_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_online_alter_log_max_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_open_files"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_optimize_fulltext_only"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_page_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_prefix_index_cluster_optimization"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_print_all_deadlocks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_purge_batch_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_purge_rseg_truncate_frequency"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_purge_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_random_read_ahead"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_read_ahead_threshold"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_read_io_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_read_only"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_replication_delay"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_rollback_on_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_rollback_segments"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_sched_priority_cleaner"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_scrub_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_scrub_log_speed"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_show_locks_held"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_show_verbose_locks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_sort_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_spin_wait_delay"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_auto_recalc"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_include_delete_marked"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_method"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_modified_counter"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_on_metadata"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_persistent"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_persistent_sample_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_sample_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_traditional"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_stats_transient_sample_pages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_status_output"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_status_output_locks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_strict_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_support_xa"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_sync_array_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_sync_spin_loops"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_table_locks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_temp_data_file_path"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_thread_concurrency"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_thread_sleep_delay"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_tmpdir"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_undo_directory"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_undo_log_truncate"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_undo_logs"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_undo_tablespaces"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_atomic_writes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_fallocate"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_global_flush_log_at_trx_commit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_mtflush"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_native_aio"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_stacktrace"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_use_trim"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "innodb_write_io_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "interactive_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "join_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "join_buffer_space_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "join_cache_level"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "keep_files_on_create"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_buffer_size"
        value        = "16777216"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_cache_age_threshold"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_cache_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_cache_division_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_cache_file_hash_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "key_cache_segments"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "lc_messages"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "lc_time_names"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "local_infile"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "lock_wait_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log-bin"
        value        = "/rdsdbdata/log/binlog/mysql-bin-changelog"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_bin_compress"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_bin_compress_min_len"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_bin_index"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_bin_trust_function_creators"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_error"
        value        = "/rdsdbdata/log/error/mysql-error.log"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_output"
        value        = "TABLE"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_queries_not_using_indexes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slave_updates"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slow_admin_statements"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slow_filter"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slow_rate_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slow_slave_statements"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_slow_verbosity"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_tc_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "log_warnings"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "long_query_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "low_priority_updates"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "lower_case_table_names"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "master_verify_checksum"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_allowed_packet"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_binlog_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_binlog_size"
        value        = "134217728"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_binlog_stmt_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_connect_errors"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_connections"
        value        = "{DBInstanceClassMemory/12582880}"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_delayed_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_digest_length"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_error_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_heap_table_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_insert_delayed_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_join_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_length_for_sort_data"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_prepared_stmt_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_recursive_iterations"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_relay_log_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_seeks_for_key"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_sort_length"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_sp_recursion_depth"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_statement_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_tmp_tables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_user_connections"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "max_write_lock_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "metadata_locks_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "min_examined_row_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "mrr_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "multi_range_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_data_pointer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_max_sort_file_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_mmap_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_repair_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_sort_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_stats_method"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "myisam_use_mmap"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "mysql56_temporal_format"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "net_buffer_length"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "net_read_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "net_retry_count"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "net_write_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "old"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "old_alter_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "old_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "old_passwords"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "optimizer_prune_level"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "optimizer_search_depth"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "optimizer_selectivity_sampling_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "optimizer_switch"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "optimizer_use_condition_selectivity"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema"
        value        = "0"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_accounts_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_digests_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_stages_history_long_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_stages_history_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_statements_history_long_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_statements_history_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_waits_history_long_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_events_waits_history_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_hosts_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_cond_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_cond_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_digest_length"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_file_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_file_handles"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_file_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_mutex_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_mutex_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_rwlock_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_rwlock_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_socket_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_socket_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_stage_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_statement_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_table_handles"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_table_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_thread_classes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_max_thread_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_session_connect_attrs_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_setup_actors_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_setup_objects_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "performance_schema_users_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "pid_file"
        value        = "/rdsdbdata/log/mysql-{EndPointPort}.pid"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "plugin_dir"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "port"
        value        = "{EndPointPort}"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "preload_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "profiling"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "profiling_history_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "progress_report_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_alloc_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_min_res_unit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_strip_comments"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_type"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_cache_wlock_invalidate"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "query_prealloc_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "range_alloc_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "read_binlog_speed_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "read_buffer_size"
        value        = "262144"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "read_only"
        value        = "{TrueIfReplica}"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "read_rnd_buffer_size"
        value        = "524288"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "relay-log"
        value        = "/rdsdbdata/log/relaylog/relaylog"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "relay_log_purge"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "relay_log_recovery"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_annotate_row_events"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_do_db"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_do_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_events_marked_for_skip"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_ignore_db"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_ignore_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "replicate_wild_ignore_table"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "rowid_merge_buff_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "secure_auth"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "secure_file_priv"
        value        = "/tmp"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "server_id"
        value        = "{ServerId}"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "session_track_schema"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "session_track_state_change"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "session_track_system_variables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "session_track_transaction_info"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "skip-slave-start"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "skip_external_locking"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "skip_name_resolve"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "skip_show_database"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_compressed_protocol"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_ddl_exec_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_domain_parallel_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_exec_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_max_allowed_packet"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_net_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_parallel_max_queued"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_parallel_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_parallel_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_run_triggers_for_rbr"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_sql_verify_checksum"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_transaction_retries"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slave_type_conversions"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slow_launch_time"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slow_query_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "slow_query_log_file"
        value        = "/rdsdbdata/log/slowquery/mysql-slowquery.log"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "socket"
        value        = "/tmp/mysql.sock"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sort_buffer_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_auto_is_null"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_big_selects"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_buffer_result"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_mode"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_notes"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_quote_show_create"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_safe_updates"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_select_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sql_warnings"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "standard_compliant_cte"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "stored_program_cache"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "strict_password_validation"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sync_binlog"
        value        = "1"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sync_frm"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sync_master_info"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sync_relay_log"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "sync_relay_log_info"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "table_definition_cache"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "table_open_cache"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "table_open_cache_instances"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_cache_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_handling"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_idle_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_max_threads"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_oversubscribe"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_prio_kickup_timer"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_priority"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_pool_stall_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "thread_stack"
        value        = "262144"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "time_zone"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tmp_disk_table_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tmp_memory_table_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tmp_table_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tmpdir"
        value        = "/rdsdbdata/tmp"
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "transaction_alloc_block_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "transaction_prealloc_size"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tx_isolation"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "tx_read_only"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "unique_checks"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "updatable_views_with_limit"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "use_stat_tables"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "userstat"
        value        = ""
        apply_method = "pending-reboot"
    }

    parameter {
        name         = "wait_timeout"
        value        = ""
        apply_method = "pending-reboot"
    }

}
