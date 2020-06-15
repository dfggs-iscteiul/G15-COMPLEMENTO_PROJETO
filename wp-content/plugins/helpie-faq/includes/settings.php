<?php

namespace HelpieFaq\Includes;

use  HelpieFaq\Includes\Settings\HRP_Getter ;
if ( !defined( 'ABSPATH' ) ) {
    exit;
}
// Exit if accessed directly

if ( !class_exists( '\\HelpieFaq\\Includes\\Settings' ) ) {
    class Settings
    {
        public function __construct()
        {
            //  error_log('Settings __construct');
            // add_action('init', [$this, 'setup_options_init']);
            // add_action('init', [$this, 'init']);
            // add_action('wp_loaded', [$this, 'wp_loaded']);
            // add_filter('csf_helpie-faq_sections', [$this, 'filter_args']);
            $this->init();
            // $this->fields = new \HelpieFaq\Includes\Settings\Fields();
        }
        
        public function filter_args( $content )
        {
            return $content;
        }
        
        public function setup_options_init()
        {
            // require_once HELPIE_FAQ_PATH . 'includes/settings/settings-config.php';
        }
        
        public function wp_loaded()
        {
        }
        
        public function init()
        {
            if ( !function_exists( '\\CSF' ) && !class_exists( '\\CSF' ) ) {
                require_once HELPIE_FAQ_PATH . 'lib/codestar-framework/codestar-framework.php';
            }
            // require_once 'settings-config.php';
            
            if ( class_exists( '\\CSF' ) ) {
                // Set a unique slug-like ID
                $prefix = 'helpie-faq';
                // Create options
                \CSF::createOptions( $prefix, array(
                    'menu_title'      => 'Settings',
                    'menu_parent'     => 'edit.php?post_type=helpie_faq',
                    'menu_type'       => 'submenu',
                    'menu_slug'       => 'helpie-review-settings',
                    'framework_title' => 'Settings',
                    'theme'           => 'light',
                    'show_search'     => false,
                ) );
                // error_log('after creat options');
                $this->general_settings( $prefix );
                $this->style_settings( $prefix );
                $this->integration_settings( $prefix );
                $this->kb_integration_settings( $prefix );
                $this->woo_integration_settings( $prefix );
            }
        
        }
        
        public function submission_settings( $prefix )
        {
            \CSF::createSection( $prefix, array(
                'id'     => 'usersubmisson',
                'title'  => __( 'User Submission', 'helpie-faq' ),
                'icon'   => 'fa fa-sign-out',
                'fields' => array(
                array(
                'id'      => 'show_submission',
                'type'    => 'switcher',
                'title'   => __( 'Submission', 'helpie-faq' ),
                'label'   => __( 'Enable / Disable User Submission form in FAQ', 'helpie-faq' ),
                'default' => true,
            ),
                array(
                'id'      => 'ask_question',
                'type'    => 'checkbox',
                'title'   => __( 'Ask Question With', 'helpie-faq' ),
                'options' => array(
                'email'  => 'Email',
                'answer' => 'Answer',
            ),
                'default' => array( 'email' ),
            ),
                array(
                'id'      => 'onsubmit',
                'title'   => __( 'On Submission', 'helpie-faq' ),
                'type'    => 'select',
                'options' => array(
                'approval'   => __( 'Dont Require Approval', 'helpie-faq' ),
                'noapproval' => __( 'Require Approval', 'helpie-faq' ),
            ),
                'info'    => __( 'Approval Before Showing', 'helpie-faq' ),
                'default' => 'noapproval',
            ),
                array(
                'type'    => 'notice',
                'class'   => 'info',
                'content' => 'Once Approved, Submitter will be notified through email',
            ),
                array(
                'id'     => 'submitter_email',
                'type'   => 'fieldset',
                'title'  => 'Submitter Notification',
                'fields' => array( array(
                'id'         => 'submitter_subject',
                'type'       => 'text',
                'title'      => __( 'Subject', 'helpie-faq' ),
                'validate'   => 'required',
                'attributes' => array(
                'placeholder' => __( 'Subject title', 'helpie-faq' ),
            ),
                'default'    => __( 'The FAQ you submitted has been approved ', 'helpie-faq' ),
            ), array(
                'id'         => 'submitter_message',
                'type'       => 'textarea',
                'title'      => __( 'Message', 'helpie-faq' ),
                'validate'   => 'required',
                'attributes' => array(
                'placeholder' => __( 'Message description', 'helpie-faq' ),
            ),
                'default'    => __( 'A new FAQ you had submitted has been approved by the admin ', 'helpie-faq' ),
            ) ),
            ),
                array(
                'id'      => 'notify_admin',
                'type'    => 'switcher',
                'title'   => __( 'Notify Admin', 'helpie-faq' ),
                'default' => true,
            ),
                array(
                'id'         => 'admin_email',
                'type'       => 'text',
                'title'      => __( 'Admin Mail', 'helpie-faq' ),
                'validate'   => 'required',
                'attributes' => array(
                'placeholder' => __( 'mail', 'helpie-faq' ),
                'type'        => 'email',
                'pattern'     => '[^@]+@[^@]+\\.[a-zA-Z]{2,6}',
            ),
                'default'    => get_option( 'admin_email' ),
                'dependency' => array( 'notify_admin', '==', 'true' ),
            )
            ),
            ) );
        }
        
        public function integration_settings( $prefix )
        {
            \CSF::createSection( $prefix, array(
                'id'    => 'integrations',
                'title' => __( 'Integrations', 'helpie-faq' ),
                'icon'  => 'fa fa-plus',
            ) );
        }
        
        public function kb_integration_settings( $prefix )
        {
            \CSF::createSection( $prefix, array(
                'parent' => 'integrations',
                'id'     => 'helpie_kb',
                'title'  => __( 'Helpie KB', 'helpie-faq' ),
                'icon'   => 'fa fa-book',
                'fields' => $this->kb_active_fields(),
            ) );
        }
        
        public function woo_integration_settings( $prefix )
        {
            \CSF::createSection( $prefix, array(
                'parent' => 'integrations',
                'id'     => 'woocommerce',
                'title'  => __( 'WooCommerce', 'helpie-faq' ),
                'icon'   => 'fa fa-cart-plus',
                'fields' => $this->woo_active_fields(),
            ) );
        }
        
        public function kb_active_fields()
        {
            if ( !\is_plugin_active( 'helpie/helpie.php' ) ) {
                $options[] = array(
                    'type'    => 'notice',
                    'class'   => 'danger',
                    'content' => __( 'In order use this feature you need to purchase and activate the <a href="https://checkout.freemius.com/mode/dialog/plugin/3014/plan/4858/?trial=paid" target="_blank">Helpie KB</a> plugin.', 'helpie-faq' ),
                );
            }
            $options[] = array(
                'id'      => 'kb_integration_switcher',
                'type'    => 'switcher',
                'title'   => __( 'Enable FAQ in Helpie KB', 'helpie-faq' ),
                'label'   => __( 'Show FAQ In Helpie KB Category Page', 'helpie-faq' ),
                'default' => true,
            );
            $options[] = array(
                'id'         => 'kb_cat_content_show',
                'type'       => 'select',
                'title'      => __( 'Show FAQ in Helpie KB Category Page', 'helpie-faq' ),
                'options'    => array(
                'before' => __( 'Before Content', 'helpie-faq' ),
                'after'  => __( 'After Content', 'helpie-faq' ),
            ),
                'default'    => 'before',
                'info'       => __( 'Select show faq before or after content in kb category page', 'helpie-faq' ),
                'dependency' => array( 'kb_integration_switcher', '==', 'true' ),
            );
            // error_log('$options : ' . print_r($options, true));
            return $options;
        }
        
        public function woo_active_fields()
        {
            if ( !\is_plugin_active( 'woocommerce/woocommerce.php' ) ) {
                $options[] = array(
                    'type'    => 'notice',
                    'class'   => 'danger',
                    'content' => __( 'In order use this feature you need to activate the <a href="/wp-admin/plugin-install.php?s=woocommerce&tab=search&type=term" target="_blank">WooCommerce</a> plugin.', 'helpie-faq' ),
                );
            }
            $options[] = array(
                'id'      => 'woo_integration_switcher',
                'type'    => 'switcher',
                'title'   => __( 'Show FAQ in WooCommerce', 'helpie-faq' ),
                'label'   => __( 'Show FAQ In WooCommerce product tab', 'helpie-faq' ),
                'default' => true,
            );
            $options[] = array(
                'id'         => 'tab_title',
                'type'       => 'text',
                'title'      => __( 'Tab Title', 'helpie-faq' ),
                'default'    => __( 'FAQ', 'helpie-faq' ),
                'dependency' => array( 'woo_integration_switcher', '==', 'true' ),
            );
            return $options;
        }
        
        public function style_settings( $prefix )
        {
            \CSF::createSection( $prefix, array(
                'id'     => 'style',
                'title'  => __( 'Style', 'helpie-faq' ),
                'icon'   => 'fa fa-paint-brush',
                'fields' => array( array(
                'id'      => 'theme',
                'type'    => 'select',
                'title'   => __( 'Theme', 'helpie-faq' ),
                'options' => array(
                'light' => __( 'Light', 'helpie-faq' ),
                'dark'  => __( 'Dark', 'helpie-faq' ),
            ),
                'default' => 'light',
                'info'    => __( 'Select Theme of FAQ Layout Section', 'helpie-faq' ),
            ) ),
            ) );
        }
        
        public function general_settings( $prefix )
        {
            $fields = array(
                array(
                'id'         => 'title',
                'type'       => 'text',
                'title'      => __( 'Title', 'helpie-faq' ),
                'attributes' => array(
                'placeholder' => __( 'FAQ Title', 'helpie-faq' ),
            ),
                'default'    => __( 'Helpie FAQ', 'helpie-faq' ),
            ),
                array(
                'id'      => 'show_search',
                'type'    => 'switcher',
                'title'   => __( 'Show Search in FAQ', 'helpie-faq' ),
                'label'   => __( 'You can search through all FAQ items', 'helpie-faq' ),
                'default' => true,
            ),
                array(
                'id'         => 'search_placeholder',
                'type'       => 'text',
                'title'      => __( 'Search Placeholder text', 'helpie-faq' ),
                'attributes' => array(
                'placeholder' => __( 'FAQ Search Placeholder text', 'helpie-faq' ),
            ),
                'dependency' => array( 'show_search', '==', 'true' ),
                'default'    => __( 'Search FAQ', 'helpie-faq' ),
            ),
                array(
                'id'      => 'toggle',
                'type'    => 'switcher',
                'title'   => __( 'Toggle', 'helpie-faq' ),
                'label'   => __( 'Toggle Open / closed Previous Item', 'helpie-faq' ),
                'default' => true,
            ),
                array(
                'id'      => 'open_first',
                'type'    => 'switcher',
                'title'   => __( 'Open First FAQ Item', 'helpie-faq' ),
                'label'   => __( 'First item open by default', 'helpie-faq' ),
                'default' => true,
            ),
                array(
                'id'      => 'display_mode',
                'title'   => __( 'Display Mode', 'helpie-faq' ),
                'default' => 'Simple Accordion',
                'options' => array(
                'simple_accordion'          => __( 'Simple Accordion', 'helpie-faq' ),
                'simple_accordion_category' => __( 'Simple Accordion by Category', 'helpie-faq' ),
                'category_accordion'        => __( 'Category Accordion', 'helpie-faq' ),
            ),
                'type'    => 'select',
            ),
                array(
                'id'      => 'sortby',
                'title'   => __( 'Sort By', 'helpie-faq' ),
                'type'    => 'select',
                'options' => array(
                'publish'      => __( 'Publish Date', 'helpie-faq' ),
                'updated'      => __( 'Updated Date', 'helpie-faq' ),
                'alphabetical' => __( 'Alphabetical', 'helpie-faq' ),
                'menu_order'   => __( 'Menu Order', 'helpie-faq' ),
            ),
                'default' => 'publish',
            ),
                array(
                'id'      => 'order',
                'title'   => __( 'Order', 'helpie-faq' ),
                'default' => 'desc',
                'options' => array(
                'asc'  => __( 'Ascending', 'helpie-faq' ),
                'desc' => __( 'Descending', 'helpie-faq' ),
            ),
                'type'    => 'select',
            ),
                array(
                'id'         => 'limit',
                'type'       => 'text',
                'title'      => __( 'Limit ( number of items )', 'helpie-faq' ),
                'default'    => 5,
                'attributes' => array(
                'min' => 1,
            ),
                'info'       => __( 'Limit of the FAQ items', 'helpie-faq' ),
            ),
                array(
                'id'      => 'enable_wpautop',
                'type'    => 'switcher',
                'title'   => __( 'Enable wpautop', 'helpie-faq' ),
                'label'   => __( 'Enable / Disable wpautop', 'helpie-faq' ),
                'default' => false,
            )
            );
            \CSF::createSection( $prefix, array(
                'id'     => 'general',
                'title'  => __( 'General', 'helpie-faq' ),
                'icon'   => 'fa fa-cogs',
                'fields' => $fields,
            ) );
        }
    
    }
    // END CLASS
}
