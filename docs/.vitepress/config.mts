import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "XLibrary",
  description: "Advanced Minecraft Core Framework Documentation",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Getting Started', link: '/getting-started' },
      { text: 'Core API', link: '/core/' },
      { text: 'Paper API', link: '/paper/' }
    ],

    sidebar: {
      '/': [
        {
          text: 'Introduction',
          items: [
            { text: 'Getting Started', link: '/getting-started' },
            { text: 'Architecture', link: '/architecture' }
          ]
        },
        {
          text: 'Core API',
          items: [
            { text: 'Pipelines', link: '/core/pipelines' },
            { text: 'Utilities', link: '/core/utilities' }
          ]
        },
        {
          text: 'Paper API',
          items: [
            { text: 'Command API', link: '/paper/commands' },
            { text: 'GUI / Menus', link: '/paper/gui' },
            { text: 'Schematics (FAWE)', link: '/paper/schematics' },
            { text: 'Utilities', link: '/paper/utilities' }
          ]
        }
      ]
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/kirilltwice' }
    ]
  }
})
