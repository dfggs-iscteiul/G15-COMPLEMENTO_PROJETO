<?php

namespace HelpieFaq\Features\Faq;

if ( !defined( 'ABSPATH' ) ) {
    exit;
}
// Exit if accessed directly

if ( !class_exists( '\\HelpieFaq\\Features\\Faq\\Fields_Model' ) ) {
    class Fields_Model
    {
        public function __construct()
        {
            $this->repo = new \HelpieFaq\Includes\Repos\Faq_Repo();
        }
        
        public function get_fields()
        {
            $fields = array(
                'title'                   => $this->get_title_field(),
                'categories'              => $this->get_categories_field(),
                'theme'                   => $this->get_theme_field(),
                'display_mode'            => $this->get_display_mode(),
                'display_category_fields' => $this->get_display_category_fields(),
                'toggle'                  => $this->get_toggle_field(),
                'open_first'              => $this->get_open_first_field(),
                'sortby'                  => $this->get_sortby_field(),
                'order'                   => $this->get_order_field(),
                'limit'                   => $this->get_limit_field(),
                'enable_wpautop'          => $this->get_enable_wpautop_field(),
                'show_search'             => $this->get_show_search_field(),
                'search_placeholder'      => $this->get_search_placeholder(),
            );
            include_once ABSPATH . 'wp-admin/includes/plugin.php';
            /* NOTE: Check for Helpie KB Plugin */
            if ( \is_plugin_active( 'helpie/helpie.php' ) ) {
                $fields['kb_categories'] = $this->get_kb_categories_field();
            }
            /* NOTE: Check for Woocommerce Plugin */
            $woo_integrator = new \HelpieFaq\Includes\Woo_Integrator();
            if ( $woo_integrator->is_woocommerce_activated() ) {
                $fields['products'] = $this->get_products_field();
            }
            return $fields;
        }
        
        public function get_default_args()
        {
            $args = array();
            // Get Default Values from GET - FIELDS
            $fields = $this->get_fields();
            foreach ( $fields as $key => $field ) {
                $args[$key] = $field['default'];
            }
            return $args;
        }
        
        protected function get_display_mode()
        {
            return array(
                'name'    => 'display_mode',
                'label'   => __( 'Display Mode', 'helpie-faq' ),
                'default' => 'simple_accordion',
                'options' => array(
                'simple_accordion'          => __( 'Simple Accordion', 'helpie-faq' ),
                'simple_accordion_category' => __( 'Simple Accordion by Category', 'helpie-faq' ),
                'category_accordion'        => __( 'Category Accordion', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        protected function get_display_category_fields()
        {
            return array(
                'name'    => 'display_category_fields',
                'label'   => __( 'Display Category Fields', 'helpie-faq' ),
                'default' => '',
                'options' => array(
                'description' => __( 'Description', 'helpie-faq' ),
            ),
                'type'    => 'multi-select',
            );
        }
        
        // FIELDS
        protected function get_title_field()
        {
            return array(
                'name'    => 'title',
                'label'   => __( 'Title', 'helpie-faq' ),
                'default' => '',
                'type'    => 'text',
            );
        }
        
        protected function get_kb_categories_field()
        {
            $options = $this->repo->get_options( 'kb-categories' );
            return array(
                'name'    => 'kb_categories',
                'label'   => __( 'KB Categories', 'helpie-faq' ),
                'default' => 'all',
                'options' => $options,
                'type'    => 'multi-select',
            );
        }
        
        public function get_toggle_field()
        {
            return array(
                'name'    => 'toggle',
                'label'   => __( 'Toggle', 'helpie-faq' ),
                'default' => 'on',
                'options' => array(
                'on'  => __( 'On', 'helpie-faq' ),
                'off' => __( 'Off', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        public function get_enable_wpautop_field()
        {
            return array(
                'name'    => 'enable_wpautop',
                'label'   => __( 'Enable wpautop', 'helpie-faq' ),
                'default' => 'off',
                'options' => array(
                'on'  => __( 'On', 'helpie-faq' ),
                'off' => __( 'Off', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        public function get_show_search_field()
        {
            return array(
                'name'    => 'show_search',
                'label'   => __( 'Show Search', 'helpie-faq' ),
                'default' => 'off',
                'options' => array(
                'on'  => __( 'On', 'helpie-faq' ),
                'off' => __( 'Off', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        public function get_theme_field()
        {
            return array(
                'name'    => 'theme',
                'label'   => __( 'Theme', 'helpie-faq' ),
                'default' => 'light',
                'options' => array(
                'light' => __( 'Light', 'helpie-faq' ),
                'dark'  => __( 'Dark', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        public function get_open_first_field()
        {
            return array(
                'name'    => 'open_first',
                'label'   => __( 'Open First FAQ Item', 'helpie-faq' ),
                'default' => 'off',
                'options' => array(
                'on'  => __( 'On', 'helpie-faq' ),
                'off' => __( 'Off', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        protected function get_products_field()
        {
            $options = $this->repo->get_options( 'woo-products' );
            return array(
                'name'    => 'products',
                'label'   => __( 'Woo Products', 'helpie-faq' ),
                'default' => 'all',
                'options' => $options,
                'type'    => 'multi-select',
            );
        }
        
        protected function get_categories_field()
        {
            $options = $this->repo->get_options( 'categories' );
            return array(
                'name'    => 'categories',
                'label'   => __( 'Categories', 'helpie-faq' ),
                'default' => 'all',
                'options' => $options,
                'type'    => 'multi-select',
            );
        }
        
        protected function get_tags_field()
        {
            return array(
                'name'    => 'tags',
                'label'   => __( 'Tags', 'helpie-faq' ),
                'default' => '',
                'type'    => 'text',
            );
        }
        
        protected function get_sortby_field()
        {
            $sortby = array(
                'name'    => 'sortby',
                'label'   => __( 'Sort By', 'helpie-faq' ),
                'default' => __( 'Publish Date', 'helpie-faq' ),
                'options' => array(
                'publish'      => __( 'Publish Date', 'helpie-faq' ),
                'updated'      => __( 'Updated Date', 'helpie-faq' ),
                'alphabetical' => __( 'Alphabetical', 'helpie-faq' ),
                'menu_order'   => __( 'Menu Order', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
            return $sortby;
        }
        
        protected function get_order_field()
        {
            return array(
                'name'    => 'order',
                'label'   => __( 'Order', 'helpie-faq' ),
                'default' => 'desc',
                'options' => array(
                'asc'  => __( 'Ascending', 'helpie-faq' ),
                'desc' => __( 'Descending', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
        
        protected function get_limit_field()
        {
            return array(
                'name'    => 'limit',
                'label'   => __( 'Limit', 'helpie-faq' ),
                'default' => 10,
                'type'    => 'number',
            );
        }
        
        protected function get_search_placeholder()
        {
            return array(
                'name'    => 'search_placeholder',
                'label'   => __( 'Search Placeholder', 'helpie-faq' ),
                'default' => __( 'Search FAQ', 'helpie-faq' ),
                'type'    => 'text',
            );
        }
        
        protected function get_submission()
        {
            return array(
                'name'    => 'show_submission',
                'label'   => __( 'Show Submission', 'helpie-faq' ),
                'default' => 'on',
                'options' => array(
                'on'  => __( 'On', 'helpie-faq' ),
                'off' => __( 'Off', 'helpie-faq' ),
            ),
                'type'    => 'select',
            );
        }
    
    }
    // END CLASS
}
