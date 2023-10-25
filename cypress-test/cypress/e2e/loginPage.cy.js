describe('Verify login page is loading', () => {
  it('Visit login page', () => {
    cy.visit('http://localhost:8080/')

    cy.get('input[id="organisation"]').type('A').should('have.value', 'A').click()
    cy.get('input[id="username"]').type('auser').should('have.value', 'auser').click()
    cy.get('input[id="password"]').type('auser').should('have.value', 'auser').click()

    cy.get('#login').click();

    
  })
})
