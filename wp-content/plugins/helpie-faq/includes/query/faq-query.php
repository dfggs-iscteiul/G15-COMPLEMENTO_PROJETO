<?php

namespace HelpieFaq\Includes\Query;

use function DI\object;

/**
 * FAQ REPO
 *
 */

if (!class_exists('\HelpieFaq\Includes\Query\Faq_Query')) {
    class Faq_Query extends \HelpieFaq\Includes\Abstracts\Object_Query
    {
        public function get_faqs()
        {
            $args = apply_filters('helpie_faq_object_query_args', $this->get_query_vars());
            $results = $this->query($args);
            return apply_filters('helpie_faq_object_query', $results, $args);
        }


        /***
         * Implements User Queries from Shortcodes 
         */
        public function query($query_vars)
        {
            $query_vars = $this->pre_query_processor($query_vars);
            $wp_query_args = $this->get_wp_query_args($query_vars);

            $results = get_posts($wp_query_args);

            // error_log('$query_vars : ' . print_r($query_vars, true));

            // Need to refactor to have 'off' and false equalised ( from settings / elementor / widgets )
            if (isset($query_vars['enable_wpautop']) && ($query_vars['enable_wpautop'] === 'on' || $query_vars['enable_wpautop']  === true)) {
                // error_log('true : ' . $query_vars['enable_wpautop']);
                foreach ($results as $post) {
                    $post->post_content = wpautop($post->post_content);
                }
            }

            return $results;
        }

        /**
         * Remove unwanted params causing problems in query
         */
        public function pre_query_processor($query_vars)
        {
            unset($query_vars['title']);
            // unset($query_vars['products']);
            return $query_vars;
        }

        /**
         * Valid default query vars for faqs.
         *
         * @return array
         */

        protected function get_default_query_vars()
        {
            return array_merge(
                parent::get_default_query_vars(),
                array(
                    'post_type' => 'helpie_faq',
                    'sortby' => 'default',
                    'numberposts' => -1,
                )
            );
        }



        protected function get_wp_query_args($query_vars)
        {
            $wp_query_args = $query_vars;
            $store = new \HelpieFaq\Includes\Stores\Faq_Store($query_vars);
            $interpreted_wp_query_args = $store->interprete($query_vars)->get();

            if ($interpreted_wp_query_args) {
                $wp_query_args = wp_parse_args($interpreted_wp_query_args, $query_vars);
            } else {
                $wp_query_args = $query_vars;
            }

            return $wp_query_args;
        }
    } // END CLASS
}