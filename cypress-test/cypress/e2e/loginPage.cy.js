describe('Verify app is still working completely', () => {
    it('Visit login page, login and logout again', () => {
        cy.visit('http://localhost:8080/')

        cy.get('input[id="organisation"]').type('A')
        cy.get('input[id="organisation"]').click()
        cy.get('input[id="organisation"]').should('have.value', 'A')

        cy.get('input[id="username"]').type('auser')
        cy.get('input[id="username"]').click()
        cy.get('input[id="username"]').should('have.value', 'auser')

        cy.get('input[id="password"]').type('auser')
        cy.get('input[id="password"]').click()
        cy.get('input[id="password"]').should('have.value', 'auser')

        cy.get('#login').click()
        cy.get('#logout').should('exist')

        cy.get('#logout').click()
    })
})
