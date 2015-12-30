module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    coffee: {
      coffee_to_js: {
        options: {
          bare: true,
          sourceMap: false
        },
        expand: true,
        flatten: false,
        cwd: "src",
        src: ["**/*.coffee"],
        dest: 'build',
        ext: ".js"
      }
    },

    copy: {
      main: {
        files: [
          {expand: true, cwd: 'src/', src: ['**/*.html', '**/*.js'], dest: 'build/'},
          {expand: true, cwd: 'lib/', src: ['**'], dest: 'build/'},
          {expand: true, cwd: 'static/', src: ['**'], dest: 'build/static'},
        ]
      }
    },

    watch: {
      scripts: {
        files: './src/**/*.coffee',
        tasks: ['compile'],
        options: {
          spawn:false,
          event:['all']
        },
      },
    },

  });

  grunt.loadNpmTasks('grunt-contrib-coffee');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.registerTask('compile', ['copy', 'coffee']);
  return null;
};
