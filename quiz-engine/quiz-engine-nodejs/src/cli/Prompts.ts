import inquirer from 'inquirer';

export class Prompts {
  /**
   * Prompt the user to select an answer (A-D/E).
   */
  static async selectAnswer(options: string[]): Promise<string> {
    const letters = ['A', 'B', 'C', 'D', 'E'];
    const choices = options.map((text, idx) => ({
      name: `${letters[idx]}) ${text}`,
      value: letters[idx],
    }));

    const { answer } = await inquirer.prompt<{ answer: string }>([
      {
        type: 'list',
        name: 'answer',
        message: 'Your answer:',
        choices,
      },
    ]);

    return answer;
  }

  /**
   * Prompt for a yes/no confirmation.
   */
  static async confirm(message: string): Promise<boolean> {
    const { confirmed } = await inquirer.prompt<{ confirmed: boolean }>([
      {
        type: 'confirm',
        name: 'confirmed',
        message,
        default: false,
      },
    ]);
    return confirmed;
  }

  /**
   * Prompt to press any key to continue.
   */
  static async pressEnterToContinue(message = 'Press Enter to continue...'): Promise<void> {
    await inquirer.prompt([
      {
        type: 'input',
        name: 'continue',
        message,
      },
    ]);
  }

  /**
   * Prompt for a file path.
   */
  static async inputPath(message: string): Promise<string> {
    const { filePath } = await inquirer.prompt<{ filePath: string }>([
      {
        type: 'input',
        name: 'filePath',
        message,
        validate: (input: string) => (input.trim().length > 0 ? true : 'Path cannot be empty'),
      },
    ]);
    return filePath.trim();
  }
}
