<?php
namespace HelpieFaq\Includes;

//
if (!defined('ABSPATH')) {
    exit; // Exit if accessed directly.
}

/**
 * Helpie-faq admin.
 *
 * Helpie-FAQ admin handler class is responsible for initializing Helpie-FAQ in
 * WordPress admin.
 *
 * @since 1.0.0
 */

class Admin
{
    public function __construct($plugin_domain, $version)
    {
        $this->plugin_domain = $plugin_domain;
        $this->version = $version;

        add_action('admin_enqueue_scripts', array($this, 'enqueue_scripts'));

        if (isset($_GET['post_type']) && $_GET['post_type'] == "helpie_faq") {
            add_action('admin_enqueue_scripts', array($this, 'set_admin_pointers'), 10, 1);
        }
    }

    public function add_management_page()
    {
        $title = __('Helpie FAQ', $this->plugin_domain);

        $hook_suffix = add_management_page($title, $title, 'export', $this->plugin_domain, array(
            $this,
            'load_admin_view',
        ));

        add_action('load-' . $hook_suffix, array($this, 'load_assets'));
    }

    public function enqueue_scripts()
    {

        wp_enqueue_style($this->plugin_domain . '-bundle-styles', HELPIE_FAQ_URL . 'assets/admin.bundle.css', array(), $this->version, 'all');
        wp_enqueue_script($this->plugin_domain . '-bundle-admin-scripts', HELPIE_FAQ_URL . 'assets/admin.bundle.js', array('jquery'), $this->version, 'all');
        $nonce = wp_create_nonce( 'helpie_faq_nonce' );
        wp_localize_script($this->plugin_domain . '-bundle-admin-scripts', 'helpie_faq_nonce', $nonce);
        wp_localize_script($this->plugin_domain . '-bundle-admin-scripts', 'my_faq_ajax_object', array('ajax_url' => admin_url('admin-ajax.php')));

        do_action('helpie_faq_admin_localize_script');
    }

    public function remove_kb_category_submenu()
    {
        remove_submenu_page('edit.php?post_type=helpie_faq', 'edit-tags.php?taxonomy=helpdesk_category&amp;post_type=helpie_faq');
    }

    public function set_admin_pointers($page)
    {
        $pointer = new \HelpieFaq\Lib\Pointers\Pointers();
        $pointers = $pointer->return_pointers();

        //Arguments: pointers php file, version (dots will be replaced), prefix
        $manager = new \HelpieFaq\Lib\Pointers\Pointers_Manager($pointers, '1.0', 'hfaq_admin_pointers');
        $manager->parse();
        $pointers = $manager->filter($page);

        if (empty($pointers)) { // nothing to do if no pointers pass the filter
            return;
        }
        wp_enqueue_style('wp-pointer');
        $js_url = HELPIE_FAQ_URL . 'lib/pointers/pointers.js';

        wp_enqueue_script('hfaq_admin_pointers', $js_url, array('wp-pointer'), null, true);
        //data to pass to javascript
        $data = array(
            'next_label' => __('Next'),
            'close_label' => __('Close'),
            'pointers' => $pointers,
        );
        wp_localize_script('hfaq_admin_pointers', 'MyAdminPointers', $data);
    }
}
