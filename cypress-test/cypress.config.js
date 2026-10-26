const { defineConfig } = require("cypress");

module.exports = defineConfig({
  defaultBrowser: 'chromium',
  video: true,
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
